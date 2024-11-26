import { Component, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Provider } from 'src/app/interfaces/catalog/provider';
import { Response } from 'src/app/interfaces/general/response';
import { ProviderService } from './provider.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { LocationService } from 'src/app/general/location.service';
import { HttpStatusCode } from '@angular/common/http';
import { Constants } from 'src/app/interfaces/general/constants';
import { Table } from 'primeng/table';

@Component({
  providers: [MessageService],
  standalone: false,
  templateUrl: './provider.component.html'
})
export class ProviderComponent implements OnInit {
    provider      : Provider = {};
    providers     : Provider[] = [];
    allProviders  : Provider[] = [];
    providerDialog: boolean = false;
  
    deleteProviderDialog: boolean = false;
    
    selectedProviders: Provider[] = [];
    submitted   : boolean = false;
  
    //Response
    response : Response<Provider[]>;
  
    loading: boolean = false;
  
    cols: any[] = [];
  
    rowsPerPageOptions = [5, 10, 20];
  
    constructor(
      private providerService: ProviderService,
      private messageService: MessageService,
      private sharedMessageService: SharedMessageService,
      private locationService      : LocationService
    ) { }
  
    ngOnInit(): void {
      this.loadProviders();
  
  //    this.locationService.getParentProviders().then(providers => {
  //      this.allProviders = providers;     
  //    });
    }
  
    loadProviders(): void {
      this.provider.status = 'active';
      this.loading       = true;
      this.providerService.getProviders(this.provider).subscribe({
        next: (data) => {
          this.response = data; 
          this.loading       = false;
          if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
            this.providers = data.payload;
          }
        },
        error: (error) => {    
          this.loading = false;                  
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting providers', error);            
      }
      });
    }
  
    openNew() {
        this.provider = {};
        this.submitted = false;
        this.providerDialog = true;
    }
  
    editProvider(provider: Provider) {
        this.provider = { ...provider };
        this.providerDialog = true;
    }
  
    deleteProvider(provider: Provider) {
        this.deleteProviderDialog = true;
        this.provider = { ...provider };
    }
    
    confirmDelete() {
        this.deleteProviderDialog = false;
        this.providerService.deleteProvider(this.provider.id).subscribe({
            next: (data) => {
                this.response = data;          
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Provider deleted successfully");            
                    this.loadProviders();           
                } else {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Provider', this.response.message);                                    
                }                    
            },
            error: (error) => {                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting provider', error);                                    
            }
        });
        this.provider = {};
    }
  
    hideDialog() {
        this.providerDialog = false;
        this.deleteProviderDialog = false;
        this.submitted = false;
    }
  
    saveProvider(): void {
      this.submitted = true;
  
      if (this.provider.name.trim()) {
        if (this.provider.id) {
          this.providerService.updateProvider(this.provider.id, this.provider).subscribe({
            next: (data: Response<any>) => {
              if (data.code === 200 && data.status === 'OK') {
                this.providers[this.findIndexById(this.provider.id)] = this.provider;
                this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Provider Updated', life: 3000 });
                this.providerDialog = false;
                this.provider = {};
              }
            },
            error: (error) => {
              this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error updating provider', life: 3000 });
            }
          });
        } else {       
          this.providerService.createProvider(this.provider).subscribe({
            next: (data: Response<any>) => {
              if (data.code === 201 && data.status === 'OK') {
                this.providers.push(data.payload);
                this.messageService.add({ severity: 'success', summary: 'Successful', detail: 'Provider Created', life: 3000 });
                this.providerDialog = false;
                this.provider = {};
              }
            },
            error: (error) => {
              this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Error creating provider', life: 3000 });
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
      for (let i = 0; i < this.providers.length; i++) {
        if (this.providers[i].id === id) {
          index = i;
          break;
        }
      }
      return index;
    }
}
