import { Component, OnInit } from '@angular/core';
import { MessageService, SelectItem } from 'primeng/api';
import { Category } from 'src/app/interfaces/catalog/category';
import { CategoryService } from './category.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { UtilsService } from 'src/app/utils/utils.service';
import { CatalogUtilService } from 'src/app/interfaces/catalog/catalog-util.service';
import { Response } from 'src/app/interfaces/general/response';
import { HttpStatusCode } from '@angular/common/http';
import { Constants } from 'src/app/interfaces/general/constants';
import { Table } from 'primeng/table';
import { ProviderService } from 'src/app/admin/provider/provider.service';
import { Provider } from 'src/app/interfaces/catalog/provider';
import { UserUtilService } from 'src/app/interfaces/users/user-util.service';

@Component({
  providers: [MessageService],
  standalone: false,
  templateUrl: './category.component.html'
})
export class CategoryComponent implements OnInit{

  //One category
  category: Category = this.catalogService.initializeCategory();

  categoryDialog: boolean = false;

  deleteCategoryDialog: boolean = false;

  //categories
  categories: Category[] = [];

  selectedCategories: Category[] = [];

  submitted: boolean = false;

  cols: any[] = [];

  rowsPerPageOptions = [5, 10, 20];

  //Response
  response : Response<Category[]>;

  loading: boolean = false;

  isNewCategory : boolean = false;

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
      private categoryService      : CategoryService, 
      private sharedMessageService : SharedMessageService,
      private providerService      : ProviderService,
      private catalogService       : CatalogUtilService,
      private utilsService         : UserUtilService
  ) { }

  ngOnInit() {     
      this.isNewCategory = false;
      this.providerCode = this.utilsService.getSessionInfoByPath('profile.providerCode') || '';
      this.role = this.utilsService.getSessionInfoByPath('role');
      this.category.providerCode = this.providerCode;
      this.getCategories(this.category);
      this.loadProviders();  
  }

  getCategories(categoryFilter: Category | null = null) {
      if (categoryFilter) {
          this.category = categoryFilter;
      }
      this.category.status       = 'active';
      this.loading               = true;
      this.categoryService.getCategories(this.category).subscribe({
          next: (data) => {
              this.response = data;         
              this.loading  = false; 
              if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                  this.categories = this.response.payload;         
              }
          },
          error: (error) => {    
              this.loading = false;                  
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting categories', error);            
          }
      });
  }

  loadProviders(): void {
    let provider: Provider = {};
    provider.status = 'active';   
    this.providerService.getProviders(provider).subscribe({
        next: (data) => {
            this.response = data;
            this.loading = false;
            if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') 
            {
                this.providerList = data.payload;
                this.providerTypes = this.providerList.map(provider => ({
                    label: provider.name,
                    value: provider.providerCode
                }));                            
                if(this.role === 'ADMIN' ) {
                    this.providerTypes.unshift({ label: 'All Providers', value: null });
                }
            }
        },
        error: (error) => {    
            this.loading = false;                  
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting providers', error);            
        }
        });
    }

  openNew() {
      this.providers        = this.providerList.map(provider => ({ label: provider.providerCode, value: provider.providerCode }));
      this.parentCategories = this.categories.map(category => ({ label: category.name, value: category.id }));
      this.category         = this.catalogService.initializeCategory();
      this.submitted = false;
      this.categoryDialog = true;
      this.isNewCategory = true;
  }

  editCategory(category: Category) {

      this.providers        = this.providerList.map(provider => ({ label: provider.providerCode, value: provider.providerCode }));
      this.parentCategories = this.categories.map(category => ({ label: category.name, value: category.id }));

      // Clone the category object to avoid mutating the original
      this.category = { ...category };

      // Set providerCode if needed
      const selectedProvider = this.providerList.find(provider => provider.id === category.providerCode);
      if (selectedProvider) {
        this.category.providerCode = selectedProvider.providerCode;
      }

      // Set categoryId for parent category dropdown
      this.category.categoryId = category.parentId ? category.parentId : null;

      this.categoryDialog = true;
      this.isNewCategory = false;
  }

  deleteCategory(category: Category) {       
      this.deleteCategoryDialog = true;
      this.category = { ...category };
  }

  confirmDelete() {
      this.deleteCategoryDialog = false;
      this.categoryService.deleteCategory(this.category.id).subscribe({
          next: (data) => {
              this.response = data;          
              if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Category deleted successfully");            
                  this.getCategories();           
              } else {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Category', this.response.message);                                    
              }                    
          },
          error: (error) => {                  
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Category', error);                                    
          }
      });
      this.category = {};
  }

  hideDialog() {
      this.categoryDialog = false;
      this.deleteCategoryDialog = false;
      this.submitted = false;
  }

  saveCategory() {

      if (
          !this.category.name ||         
          !this.category.providerCode
      ) {
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Validation Error', 'All fields are required');
          return;
      }      

      this.submitted = true;
      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.INFO, 'Info', "Category is been created or updated",0);

      if (this.category.id)
      {
          this.categoryService.updateCategory(this.category.id,this.category).subscribe({
              next: (data) => {
                  this.response = data;          
                  if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') 
                  {
                      this.sharedMessageService.clearMessages();
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Category updated");            
                      this.getCategories();           
                  } 
                  else 
                  {
                      this.sharedMessageService.clearMessages();
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Category', this.response.message);                                    
                  }                    
              },
              error: (error) => {                  
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Category', error);                                    
              }
          });
      } else {
          this.categoryService.createCategory(this.category).subscribe({
              next: (data) => {
                  this.response = data;          
                  if (this.response.code === HttpStatusCode.Created && this.response.status === 'OK') 
                  {
                      this.sharedMessageService.clearMessages();
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Category created");                                    
                      this.getCategories();           
                  } 
                  else 
                  {
                      this.sharedMessageService.clearMessages();
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Category', this.response.message);                                    
                  }                    
              },
              error: (error) => {  
                  this.sharedMessageService.clearMessages();
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Category', error);                                    
              }
          });               
      }

      this.categoryDialog = false;
      this.category = {};
  }            

  // Habilita el filtro global en la tabla PrimeNG
  onGlobalFilter(table: Table, event: Event) {
     const value = (event.target as HTMLInputElement).value;
     table.filterGlobal(value, 'contains');
  }

    onSearchProviderFilter(event: any) {
       this.category.providerCode = event.value? event.value : '';
       this.getCategories(this.category);
    }
}



