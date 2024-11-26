import { HttpStatusCode } from '@angular/common/http';
import { Component, Provider } from '@angular/core';
import { SelectItem } from 'primeng/api';
import { ProviderService } from 'src/app/admin/provider/provider.service';
import { CatalogUtilService } from 'src/app/interfaces/catalog/catalog-util.service';
import { Characteristic } from 'src/app/interfaces/catalog/characteristic';
import { Constants } from 'src/app/interfaces/general/constants';
import { UserUtilService } from 'src/app/interfaces/users/user-util.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { Response } from 'src/app/interfaces/general/response';
import { CharacteristicService } from './characteristic.service';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-characteristic',
  standalone: false, 
  templateUrl: './characteristic.component.html'
})
export class CharacteristicComponent {

    //One characteristic
    characteristic: Characteristic = this.catalogService.initializeCharacteristic();

    characteristicDialog: boolean = false;

    deleteCharacteristicDialog: boolean = false;

    //characteristics
    characteristics: Characteristic[] = [];

    selectedCharacteristics: Characteristic[] = [];

    submitted: boolean = false;
  
    cols: any[] = [];
  
    rowsPerPageOptions = [5, 10, 20];
  
    //Response
    response : Response<Characteristic[]>;
  
    loading: boolean = false;

    isNewCharacteristic : boolean = false;

    providerList: Provider[] = [];
  
    role : string = '';
  
    //Provider List
    providers: any[] = [];
  
    //Parent Category List
    parentCategories: any[] = [];
  
    providerCode: string = '';
      
    providerResponse: Response<Provider[]>;
    
    providerTypes: SelectItem[] = [];
  
    constructor(
        private characteristicService: CharacteristicService, 
        private sharedMessageService : SharedMessageService,
        private providerService      : ProviderService,
        private catalogService       : CatalogUtilService,
        private utilsService         : UserUtilService
    ) { }
  
    ngOnInit() {     
        this.isNewCharacteristic = false;
        this.getCharacteristics();
    }

    getCharacteristics(characteristicFilter: Characteristic | null = null) {
        if (characteristicFilter) {
            this.characteristic = characteristicFilter;
        }
        this.characteristic.status       = 'active';
        this.loading               = true;
        this.characteristicService.getCharacteristics(this.characteristic).subscribe({
            next: (data) => {
                this.response = data;         
                this.loading  = false; 
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.characteristics = this.response.payload;         
                }
            },
            error: (error) => {    
                this.loading = false;                  
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting characteristics', error);            
            }
        });
    }

    openNew() {
        this.characteristic = this.catalogService.initializeCharacteristic();
        this.submitted = false;
        this.characteristicDialog = true;
        this.isNewCharacteristic = true;
    }

    editCharacteristic(characteristic: Characteristic) {        
        // Clone the characteristic object to avoid mutating the original
        this.characteristic = { ...characteristic };

        this.characteristicDialog = true;
        this.isNewCharacteristic = false;
    }

    deleteCharacteristic(characteristic: Characteristic) {
        this.deleteCharacteristicDialog = true;
        this.characteristic = { ...characteristic };
    }
  
    confirmDelete() {
        this.deleteCharacteristicDialog = false;
        this.characteristicService.deleteCharacteristic(this.characteristic.id).subscribe({
            next: (data) => {
                this.response = data;          
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Characteristic deleted successfully");
                    this.getCharacteristics();
                } else {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Characteristic', this.response.message);
                }
            },
            error: (error) => {
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Characteristic', error);
            }
        });
        this.characteristic = {};
    }
  
    hideDialog() {
        this.characteristicDialog = false;
        this.deleteCharacteristicDialog = false;
        this.submitted = false;
    }

    saveCharacteristic() {

        if (
            !this.characteristic.name
        ) {
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Validation Error', 'All fields are required');
            return;
        }      
  
        this.submitted = true;
        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.INFO, 'Info', "Characteristic is been created or updated",0);

        if (this.characteristic.id)
        {
            this.characteristicService.updateCharacteristic(this.characteristic.id,this.characteristic).subscribe({
                next: (data) => {
                    this.response = data;          
                    if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') 
                    {
                        this.sharedMessageService.clearMessages();
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Characteristic updated");
                        this.getCharacteristics();
                    }
                    else
                    {
                        this.sharedMessageService.clearMessages();
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Characteristic', this.response.message);
                    }                    
                },
                error: (error) => {
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Characteristic', error);
                }
            });
        } else {
            this.characteristicService.createCharacteristic(this.characteristic).subscribe({
                next: (data) => {
                    this.response = data;          
                    if (this.response.code === HttpStatusCode.Created && this.response.status === 'OK') 
                    {
                        this.sharedMessageService.clearMessages();
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Characteristic created");
                        this.getCharacteristics();
                    }
                    else
                    {
                        this.sharedMessageService.clearMessages();
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Characteristic', this.response.message);
                    }                    
                },
                error: (error) => {  
                    this.sharedMessageService.clearMessages();
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Characteristic', error);
                }
            });               
        }

        this.characteristicDialog = false;
        this.characteristic = {};
    }

    // Habilita el filtro global en la tabla PrimeNG
    onGlobalFilter(table: Table, event: Event) {
       const value = (event.target as HTMLInputElement).value;
       table.filterGlobal(value, 'contains');
    }

}
