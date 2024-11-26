import { Component, OnInit } from '@angular/core';
import { RegionService } from './region.service';
import { MessageService } from 'primeng/api';
import { Response } from 'src/app/interfaces/general/response';
import { Region } from 'src/app/interfaces/catalog/region';
import { Table } from 'primeng/table';
import { HttpStatusCode } from '@angular/common/http';
import { Constants } from 'src/app/interfaces/general/constants';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { LocationService } from 'src/app/general/location.service';


@Component({
  templateUrl: './region.component.html',
  providers: [MessageService],
  standalone: false
})
export class RegionComponent implements OnInit {

  region      : Region = {};
  regions     : Region[] = [];
  allRegions  : Region[] = [];
  regionDialog: boolean = false;

  deleteRegionDialog: boolean = false;
  
  selectedRegions: Region[] = [];
  submitted   : boolean = false;

  //Response
  response : Response<Region[]>;

  loading: boolean = false;

  cols: any[] = [];

  rowsPerPageOptions = [5, 10, 20];

  constructor(
    private regionService: RegionService,
    private messageService: MessageService,
    private sharedMessageService: SharedMessageService,
    private locationService      : LocationService
  ) { }

  ngOnInit(): void {
    this.loadRegions();

    this.locationService.getParentRegions().then(regions => {
      this.allRegions = regions;     
    });
  }

  loadRegions(): void {
    this.region.status = 'active';
    this.loading       = true;
    this.regionService.getRegions(this.region).subscribe({
      next: (data) => {
        this.response = data; 
        this.loading       = false;
        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
          this.regions = data.payload;
        }
      },
      error: (error) => {    
        this.loading = false;                  
        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting regions', error);            
    }
    });
  }

  openNew() {
      this.region = {};
      this.submitted = false;
      this.regionDialog = true;
  }

  editRegion(region: Region) {
      this.region = { ...region };
      this.regionDialog = true;
  }

  deleteRegion(region: Region) {
      this.deleteRegionDialog = true;
      this.region = { ...region };
  }
  
  confirmDelete() {
      this.deleteRegionDialog = false;
      this.regionService.deleteRegion(this.region.id).subscribe({
          next: (data) => {
              this.response = data;          
              if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Region deleted successfully");            
                  this.loadRegions();           
              } else {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Region', this.response.message);                                    
              }                    
          },
          error: (error) => {                  
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting region', error);                                    
          }
      });
      this.region = {};
  }

  hideDialog() {
      this.regionDialog = false;
      this.deleteRegionDialog = false;
      this.submitted = false;
  }

  saveRegion(): void {
    this.submitted = true;

    if (this.region.name.trim()) {
      if (this.region.id) {
        this.regionService.updateRegion(this.region.id, this.region).subscribe({
          next: (data: Response<any>) => {
            if (data.code === 200 && data.status === 'OK') {
              this.regions[this.findIndexById(this.region.id)] = this.region;
              this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Region Updated', life: 3000 });
              this.regionDialog = false;
              this.region = {};
            }
          },
          error: (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating region', life: 3000 });
          }
        });
      } else {       
        this.regionService.createRegion(this.region).subscribe({
          next: (data: Response<any>) => {
            if (data.code === 201 && data.status === 'OK') {
              this.regions.push(data.payload);
              this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Region Created', life: 3000 });
              this.regionDialog = false;
              this.region = {};
            }
          },
          error: (error) => {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error creating region', life: 3000 });
          }
        });
      }
    }
  }

  onGlobalFilter(table: Table, event: Event) {
          table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
      }

  findIndexById(id: string): number {
    let index = -1;
    for (let i = 0; i < this.regions.length; i++) {
      if (this.regions[i].id === id) {
        index = i;
        break;
      }
    }
    return index;
  }
}