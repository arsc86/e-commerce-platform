import { Component, OnInit } from '@angular/core';
import { MessageService, SelectItem } from 'primeng/api';
import { Warehouse } from 'src/app/interfaces/catalog/warehouse';
import { Response } from 'src/app/interfaces/general/response';
import { WarehouseService } from './warehouse.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { LocationService } from 'src/app/general/location.service';
import { HttpStatusCode } from '@angular/common/http';
import { Constants } from 'src/app/interfaces/general/constants';
import { Table } from 'primeng/table';
import { environment } from 'src/environments/environment';

@Component({
  providers: [MessageService],
  templateUrl: './warehouse.component.html'
})
export class WarehouseComponent implements OnInit {

      warehouse      : Warehouse = {};
      warehouses     : Warehouse[] = [];
      regions        : SelectItem[] = [];
      warehouseDialog: boolean = false;
    
      deleteWarehouseDialog: boolean = false;
      
      selectedWarehouses: Warehouse[] = [];
      submitted   : boolean = false;
    
      //Response
      response : Response<Warehouse[]>;
    
      loading: boolean = false;
    
      cols: any[] = [];
    
      rowsPerPageOptions = [5, 10, 20];

      //google maps
      center = { 
        lat: environment.BASE_COORDINATES.lat, 
        lng: environment.BASE_COORDINATES.lng 
      };
      zoom   = 12;
      selectedLocation: { lat: number; lng: number } | null = null;
      viewMap         : boolean = false;
    
      constructor(
        private warehouseService: WarehouseService,
        private messageService: MessageService,
        private sharedMessageService: SharedMessageService,
        private locationService      : LocationService
      ) { }
    
      ngOnInit(): void {
        this.loadWarehouses();
    
        this.locationService.getRegion().subscribe({
          next: (data) => {
              this.response = data;                         
              if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {                            
                this.regions = this.response.payload.map(region => {
                  return { label: region.name, value: region.id };
                });
              }
          },
          error: (error) => {                                     
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting Regions', error);            
          }
      });
      }
    
      loadWarehouses(): void {
        this.warehouse.status = 'active';
        this.loading       = true;
        this.warehouseService.getWarehouses(this.warehouse).subscribe({
          next: (data) => {
            this.response = data; 
            this.loading       = false;
            if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
              this.warehouses = data.payload;
            }
          },
          error: (error) => {    
            this.loading = false;                  
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting warehouses', error);            
        }
        });
      }
    
      openNew() {
          this.warehouse = {};
          this.submitted = false;
          this.warehouseDialog = true;
      }
    
      editWarehouse(warehouse: Warehouse) {
          this.warehouse = { ...warehouse };
          this.warehouseDialog = true;
          if(!this.warehouse.coordinates) {  
            this.center = { 
              lat: environment.BASE_COORDINATES.lat, 
              lng: environment.BASE_COORDINATES.lng 
            };
          }
          else{
            const [lat, lng] = this.warehouse.coordinates.split(',').map(coord => parseFloat(coord.trim()));
            this.center = { lat, lng };
            this.selectedLocation = { lat, lng };
          }
      }
    
      deleteWarehouse(warehouse: Warehouse) {
          this.deleteWarehouseDialog = true;
          this.warehouse = { ...warehouse };
      }
      
      confirmDelete() {
          this.deleteWarehouseDialog = false;
          this.warehouseService.deleteWarehouse(this.warehouse.id).subscribe({
              next: (data) => {
                  this.response = data;          
                  if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Warehouse deleted successfully");            
                      this.loadWarehouses();           
                  } else {
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Warehouse', this.response.message);                                    
                  }                    
              },
              error: (error) => {                  
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting warehouse', error);                                    
              }
          });
          this.warehouse = {};
      }
    
      hideDialog() {
          this.warehouseDialog = false;
          this.deleteWarehouseDialog = false;
          this.submitted = false;
      }
    
      saveWarehouse(): void {
        this.submitted = true;
        
        if (!this.warehouse.name || 
            !this.warehouse.address || 
            !this.warehouse.coordinates || 
            !this.warehouse.locationId ||
            !this.warehouse.deliveryTime) {
          this.messageService.add({ severity: 'warn', summary: 'Warning', detail: 'Please fill in all required fields', life: 3000 });
          return;
        }                     

        this.warehouse.location = this.warehouse.locationId;
    
        if (this.warehouse.name.trim()) {
          if (this.warehouse.id) 
          {
            console.log(this.warehouse);
            if(this.warehouse.coordinates) {  
              const [lat, lng] = this.warehouse.coordinates.split(',').map(coord => parseFloat(coord.trim()));
              this.center = { lat, lng };
            }
           
            this.warehouseService.updateWarehouse(this.warehouse.id, this.warehouse).subscribe({
              next: (data: Response<any>) => {
                if (data.code === 201 && data.status === 'OK') {
                  this.warehouse = data.payload;                 
                  this.warehouses[this.findIndexById(this.warehouse.id)] = this.warehouse;
                  this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Warehouse Updated', life: 3000 });
                  this.warehouseDialog = false;
                  this.warehouse = {};
                }
              },
              error: (error) => {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating warehouse', life: 3000 });
              }
            });
          } else {       
            this.warehouseService.createWarehouse(this.warehouse).subscribe({
              next: (data: Response<any>) => {
                if (data.code === 201 && data.status === 'OK') {
                  this.warehouses.push(data.payload);
                  this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Warehouse Created', life: 3000 });
                  this.warehouseDialog = false;
                  this.warehouse = {};
                }
              },
              error: (error) => {
                this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error creating warehouse', life: 3000 });
              }
            });
          }

          this.submitted = false;
        }
      }
    
  onGlobalFilter(table: Table, event: Event) {
          table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
      }

  findIndexById(id: string): number {
    let index = -1;
    for (let i = 0; i < this.warehouses.length; i++) {
      if (this.warehouses[i].id === id) {
        index = i;
        break;
      }
    }
    return index;
  }

  //Google Maps
  onMapClick(event: google.maps.MapMouseEvent) {    
    if (event.latLng) {
      this.selectedLocation = {
        lat: event.latLng.lat(),
        lng: event.latLng.lng()
      };
      this.viewMap = false;
      this.warehouse.coordinates = this.selectedLocation.lat.toFixed(4) + ',' + this.selectedLocation.lng.toFixed(4);
    }
  }

  openMap(coordinates : string): void {
    this.viewMap = true;    
    if(coordinates) {
      const [lat, lng] = coordinates.split(',').map(coord => parseFloat(coord.trim()));
      this.center = { lat, lng };      
      this.selectedLocation = { lat, lng };
    }
  }
}
