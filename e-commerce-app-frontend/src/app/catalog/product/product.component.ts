import { Component } from '@angular/core';
import { Category } from 'src/app/interfaces/catalog/category';
import { Response } from 'src/app/interfaces/general/response';
import { Characteristic } from 'src/app/interfaces/catalog/characteristic';
import { SelectItem } from 'primeng/api';
import { ProductService } from './product.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { CatalogUtilService } from 'src/app/interfaces/catalog/catalog-util.service';
import { ProviderService } from 'src/app/admin/provider/provider.service';
import { HttpStatusCode } from '@angular/common/http';
import { Constants } from 'src/app/interfaces/general/constants';
import { Table } from 'primeng/table';
import { UserUtilService } from 'src/app/interfaces/users/user-util.service';
import { Product, ProductCreateRequest, ProductResponse } from 'src/app/interfaces/catalog/product';
import { CategoryService } from '../category/category.service';
import { CharacteristicService } from '../characteristic/characteristic.service';
import { Provider } from 'src/app/interfaces/catalog/provider';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-product',
  standalone: false,
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent {

  //One product
      product: Product = this.catalogService.initializeProduct();

      productCreateRequest: ProductCreateRequest = this.catalogService.initializeProductCreateRequest();

      productDialog: boolean = false;

      deleteProductDialog: boolean = false;

      //products
      products: Product[] = [];

      selectedProducts: Product[] = [];

      submitted: boolean = false;
    
      cols: any[] = [];

      totalRecords: number = 0;

      page     : number = 0;
      pageSize : number = 5; 
    
      //Response
      response         : Response<Product[]>;
      productResponse  : Response<ProductResponse>;

      loading: boolean = false;
      
      role : string = '';

      //---------------------------------------------------------
      //Creation and Update Product
      //---------------------------------------------------------

      isNewProduct: boolean = false;
      showNewProductButton: boolean = false;

      //Categories
      categoryResponse: Response<Category[]>;
      categoryTypes: SelectItem[] = [];

      //Providers      
      providerCode: string = '';          
      providerResponse: Response<Provider[]>;        
      providerTypes: SelectItem[] = [];
      matchedProvider : string = '';

      uploadedFiles: any[] = [];

      //Characteristics
      characteristicResponse: Response<Characteristic[]>;
      characteristicTypes: SelectItem[] = [];

      constructor(
          private productService       : ProductService, 
          private categoryService      : CategoryService, 
          private characteristicService : CharacteristicService,
          private sharedMessageService : SharedMessageService,
          private providerService      : ProviderService,
          private catalogService       : CatalogUtilService,
          private utilsService         : UserUtilService
      ) { }
    
      ngOnInit() {     
          this.isNewProduct = false;  
          this.providerCode = this.utilsService.getSessionInfoByPath('profile.providerCode') || '';               
          this.role = this.utilsService.getSessionInfoByPath('role');        
          const productRequest : ProductCreateRequest = {
              product : this.catalogService.initializeProduct(),
              pageNumber: 0,
              pageSize: 5
          };
          
          this.getProducts(productRequest);

          this.loadCategories();
          this.loadProviders();
          this.loadCharacteristics();

          this.product.code = this.generateProductCode(this.providerCode);

          console.log('Provider Code:', this.product);
      }

      loadCategories() {
          const category : Category = this.catalogService.initializeCategory();
          category.status       = 'active';        
          this.categoryService.getCategories(category).subscribe({
              next: (data) => {
                  this.categoryResponse = data;                           
                  if (this.categoryResponse.code == HttpStatusCode.Ok && this.categoryResponse.status === 'OK') {
                      this.categoryTypes = this.categoryResponse.payload.map(cat => ({ label: cat.name, value: cat.id }));
                  }
              },
              error: (error) => {                     
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting categories', error);            
              }
          });
      }

      loadCharacteristics() {
          const characteristic : Characteristic = this.catalogService.initializeCharacteristic();
          characteristic.status       = 'active';        
          this.characteristicService.getCharacteristics(characteristic).subscribe({
              next: (data) => {
                  this.characteristicResponse = data;                           
                  if (this.characteristicResponse.code == HttpStatusCode.Ok && this.characteristicResponse.status === 'OK') {
                      this.characteristicTypes = this.characteristicResponse.payload.map(cat => ({ label: cat.name, value: cat.id }));
                  }
              },
              error: (error) => {                     
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting characteristics', error);            
              }
          });
      }

      loadProviders(): void {
          const provider: Provider = {};
          provider.status = 'active';   
          if(this.role !== 'ADMIN' )
          {
              provider.providerCode = this.providerCode;
          }
          this.providerService.getProviders(provider).subscribe({
              next: (data) => {
                  this.providerResponse = data;
                  if (this.providerResponse.code == HttpStatusCode.Ok && this.providerResponse.status === 'OK') 
                  {                   
                      this.providerTypes = this.providerResponse.payload.map(provider => ({ label: provider.name, value: provider.name }));                                 
                  }
              },
              error: (error) => {                    
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting providers', error);            
              }
              });
      }

      getProducts(productRequest: ProductCreateRequest | null = null) {

          if (productRequest) {
              this.productCreateRequest = productRequest;
          }
          else
          {
              this.productCreateRequest.product.status = 'active';
          }
          
          this.loading               = true;
          this.productService.getProducts(this.productCreateRequest).subscribe({
              next: (data) => {
                  this.productResponse = data;                  
                  this.loading  = false;
                  if (this.productResponse.code == HttpStatusCode.Ok && this.productResponse.status === 'OK') {
                      this.products     = this.productResponse.payload.content;                
                  }
              },
              error: (error) => {    
                  this.loading = false;                  
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting products', error);            
              }
          });
      }
  
      openNew() {
          this.product = this.catalogService.initializeProduct();
          this.submitted = false;
          //this.productDialog = true;
          this.isNewProduct = true;
          this.showNewProductButton = true;

          if (this.providerTypes.length > 0) {
            const matchedProvider = this.providerResponse?.payload?.find(
                provider => provider.providerCode === this.providerCode
            );
            this.matchedProvider = matchedProvider?.id || "0";
            this.product.code = this.generateProductCode(this.matchedProvider);
          }          
      }

      editProduct(product: Product) {
          // Clone the product object to avoid mutating the original
          this.product = { ...product };

          this.productDialog = true;
          this.isNewProduct = false;
      }

      deleteProduct(product: Product) {
          this.deleteProductDialog = true;
          this.product = { ...product };
      }
    
      confirmDelete() {
          this.deleteProductDialog = false;
          this.productService.deleteProduct(this.product.id).subscribe({
              next: (data) => {
                  this.response = data;          
                  if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Product deleted successfully");
                      this.getProducts();
                  } else {
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Product', this.response.message);
                  }
              },
              error: (error) => {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error deleting Product', error);
              }
          });
          this.product = {};
      }
    
      hideDialog() {
          this.productDialog = false;
          this.deleteProductDialog = false;
          this.submitted = false;
      }

      saveProduct() {

          if (
              !this.product.name
          ) {
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Validation Error', 'All fields are required');
              return;
          }      
    
          this.submitted = true;
          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.INFO, 'Info', "Product is been created or updated",0);

          if (this.product.id)
          {
              this.productService.updateProduct(this.product.id,this.product).subscribe({
                  next: (data) => {
                      this.response = data;          
                      if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') 
                      {
                          this.sharedMessageService.clearMessages();
                          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Product updated");
                          this.getProducts();
                      }
                      else
                      {
                          this.sharedMessageService.clearMessages();
                          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Product', this.response.message);
                      }                    
                  },
                  error: (error) => {
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error updating Characteristic', error);
                  }
              });
          } else {
            console.log('Creating product:', this.product);
              /*this.productService.createProduct(this.product).subscribe({
                  next: (data) => {
                      this.response = data;          
                      if (this.response.code === HttpStatusCode.Created && this.response.status === 'OK') 
                      {
                          this.sharedMessageService.clearMessages();
                          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Successful', "Product created");
                          this.getProducts();
                      }
                      else
                      {
                          this.sharedMessageService.clearMessages();
                          this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Product', this.response.message);
                      }                    
                  },
                  error: (error) => {  
                      this.sharedMessageService.clearMessages();
                      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating a new Product', error);
                  }
              });  */             
          }

          this.productDialog = false;
          this.product = {};
      }
  
    // Habilita el filtro global en la tabla PrimeNG
    onGlobalFilter(table: Table, event: Event) {
         const value = (event.target as HTMLInputElement).value;
         table.filterGlobal(value, 'contains');
    }

    searchProduct() {          
          const productRequest: ProductCreateRequest = {
              product: this.productCreateRequest.product,
              pageNumber: this.page,
              pageSize: this.pageSize
          };

          this.getProducts(productRequest);
    }

    resetFilters() {
        this.page = 0;
        this.productCreateRequest = {
            product: this.catalogService.initializeProduct(),
            pageNumber: 0,
            pageSize: 5
        };
        this.getProducts(this.productCreateRequest);
    }

    loadNextPage() {
        this.page+=5;
        this.searchProduct();
    }

    loadPreviousPage() {
        if (this.page > 0) {
          this.page-=5;
          this.searchProduct();
        }
    }

    onToggleRow(product, dt) {
      if (!dt.isRowExpanded(product)) {
          dt.toggleRow(product); // Only toggle the row if it's being opened
          this.loadPriceAndDiscounts(product);
      } else {
          dt.toggleRow(product); // Close the row without triggering a search
          return;
      }
    }

    onTabChange(product: any, event: any)
    {
      const selectedIndex = event.index;

      let characteristicsLink = product.links?.find(link => link.rel === 'characteristics');
      let inventoryLink       = product.links?.find(link => link.rel === 'inventory');      

      switch (selectedIndex) {
        case 0: // Price/Discounts tab
          this.loadPriceAndDiscounts(product);
          break;
        case 1: // Characteristics tab
          if (characteristicsLink && (!product.characteristics || product.characteristics.length === 0)) {
            this.productService.executeHateoasLink(characteristicsLink.href).subscribe({
              next: (data) => {
                const response = data;
                if (response.code == HttpStatusCode.Ok && response.status === 'OK')
                {
                  product.characteristics = response.payload.map((char) =>
                  {
                    if (char.characteristicsValues)
                    {
                      char.characteristicsValues = char.characteristicsValues.map((value) => {
                        if (value.files > 0 && value.links && value.links.length > 0) {
                          const fileLink = value.links.find(link => link.rel === 'file');
                          if (fileLink)
                          {
                            this.productService.executeHateoasLink(fileLink.href).subscribe({
                              next: (fileData) => {
                                if (fileData && fileData.payload && fileData.payload.file)
                                {
                                  value.fileBase64 = `data:image/png;base64,${fileData.payload.file}`;
                                }
                              },
                              error: (error) => {
                                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error loading file', error);
                              }
                            });
                          }
                        }
                        return value;
                      });
                    }
                    return char;
                  });
                } else {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting characteristics', response.message);
                }
              },
              error: (error) => {
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting characteristics', error);
              }
            });
          }
          break;
        case 2: // Inventory tab
          if (inventoryLink && (!product.inventory || product.inventory.length === 0)) {
            this.productService.executeHateoasLink(inventoryLink.href).subscribe({
              next: (data) => {
                const response = data;
                if (response.code == HttpStatusCode.Ok && response.status === 'OK') {
                  product.inventory = response.payload;
                } else {
                  this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting inventory', response.message);
                }
              },
              error: (error) => {
                this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting inventory', error);
              }
            });
          }
          break;
        default:
          break;
      }
    }

    loadPriceAndDiscounts(product: any) {
      let priceLink = product.links?.find(link => link.rel === 'price');
      let discountLink = product.links?.find(link => link.rel === 'discount');

      if (priceLink && (!product.prices || product.prices.length === 0)) {
        this.productService.executeHateoasLink(priceLink.href).subscribe({
          next: (data) => {
            const response = data;
            if (response.code == HttpStatusCode.Ok && response.status === 'OK') {
              product.prices = response.payload;
            } else {
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting prices', response.message);
            }
          },
          error: (error) => {
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting prices', error);
          }
        });
      }

      if (discountLink && (!product.discounts || product.discounts.length === 0)) {
        this.productService.executeHateoasLink(discountLink.href).subscribe({
          next: (data) => {
            const response = data;
            if (response.code == HttpStatusCode.Ok && response.status === 'OK') {
              product.discounts = response.payload;
            } else {
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting discounts', response.message);
            }
          },
          error: (error) => {
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting discounts', error);
          }
        });
      }
    }

    addCharacteristic() {
      this.product.characteristics = this.product.characteristics || [];
      this.product.characteristics.push({
        characteristicsNames: '',
        characteristicsValues: []
      });
    }

    addCharacteristicValue(index: number) {
      this.product.characteristics[index].characteristicsValues = this.product.characteristics[index].characteristicsValues || [];
      this.product.characteristics[index].characteristicsValues.push({
        value: '',
        file: null
      });
    }

    removeCharacteristicValue(characteristicIndex: number, valueIndex: number) {
      this.product.characteristics[characteristicIndex].characteristicsValues.splice(valueIndex, 1);
    }

    onImageUpload(event: Event, characteristicIndex: number, valueIndex: number) {
      const file = (event.target as HTMLInputElement).files?.[0];     
      if (file) {               
          // Si no hay characteristic ID, solo mostrar preview
          const reader = new FileReader();
          reader.onload = () => {
            const base64 = reader.result as string;
            this.product.characteristics[characteristicIndex].characteristicsValues[valueIndex].file = file;
            this.product.characteristics[characteristicIndex].characteristicsValues[valueIndex].fileBase64 = base64;
            this.product.characteristics[characteristicIndex].characteristicsValues[valueIndex].fileName = file.name;
          };
          reader.readAsDataURL(file);        
      }
    }

    uploadAllCharacteristicFiles(): Observable<any>[] {
      const uploadObservables: Observable<any>[] = [];
      
      this.product.characteristics?.forEach((characteristic, charIndex) => {
        characteristic.characteristicsValues?.forEach((value, valueIndex) => {
          if (value.file && characteristic.characteristicId) {
            const upload$ = this.productService.uploadCharacteristicFile(
              characteristic.characteristicId, 
              value.file, 
              'IMAGE'
            );
            uploadObservables.push(upload$);
          }
        });
      });
      
      return uploadObservables;
    }

    addPrice() {
      this.product.prices = this.product.prices || [];
      this.product.prices.push({
        location: '',
        value: 0,
        startDate: '',
        endDate: '',
        type: '',
        id: 0,
        locationId: 0,
        status: '',
        createdAt: ''
      });
    }

    removePrice(index: number) {
      this.product.prices.splice(index, 1);
    }

    addDiscount() {
      this.product.discounts = this.product.discounts || [];
      this.product.discounts.push({
        location: '',
        value: 0,
        startDate: '',
        endDate: '',
        type: '',
        id: 0,
        locationId: 0,
        status: '',
        createdAt: ''
      });
    }

    removeDiscount(index: number) {
      this.product.discounts.splice(index, 1);
    }

    addInventoryItem() {
      this.product.inventory = this.product.inventory || [];
      this.product.inventory.push({
        warehouse: '',
        quantity: 0,
        warehouseId: 0
      });
    }

    removeInventoryItem(index: number) {
      this.product.inventory.splice(index, 1);
    }

    cancelProduct() {
      this.product = this.catalogService.initializeProduct();
      this.showNewProductButton = false;
    }

    generateProductCode(providerId: string, categoryId?: string): string {
        const prefix = "PROD-COD-PROV";
        
        // Generar parte aleatoria de 6 caracteres alfanuméricos
        const randomPart = Array.from({ length: 6 }, () =>
            Math.random().toString(36).charAt(2).toUpperCase()
        ).join("");

        // Si se proporciona categoryId, incluirlo en el formato
        if (categoryId) {
            return `${prefix}-${providerId}-CAT-${categoryId}-${randomPart}`;
        } else {
            // Formato original si no hay categoría
            return `${prefix}-${providerId}-${randomPart}`;
        }
    }

    onProviderChange(event: any) {
        const selectedProviderName = event.value;
        
        if (selectedProviderName && this.providerResponse?.payload) {
            const selectedProvider = this.providerResponse.payload.find(
                provider => provider.name === selectedProviderName
            );
            
            if (selectedProvider) {
                // Incluir categoryId si está seleccionado
                this.product.code = this.generateProductCode(
                    selectedProvider.id, 
                    this.product.categoryId?.toString()
                );
            }
        }
    }

    onCategoryChange(event: any) {
        const selectedCategoryId = event.value;       
        if (this.product.providerName && this.providerResponse?.payload) {
            const selectedProvider = this.providerResponse.payload.find(
                provider => provider.name === this.product.providerName
            );

            this.product.code = this.generateProductCode(
                    selectedProvider.id?selectedProvider.id:this.product.code, 
                    selectedCategoryId?.toString()
            );
        }
        else
        {         
            this.product.code = this.generateProductCode(
                    this.matchedProvider, 
                    selectedCategoryId?.toString()
              );
        }
    }
}
