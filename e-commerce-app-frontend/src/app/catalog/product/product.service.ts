import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Characteristic } from 'src/app/interfaces/catalog/characteristic';
import { Product, ProductCharacteristic, ProductCreateRequest, ProductDiscountPrice, ProductResponse, ProductStock } from 'src/app/interfaces/catalog/product';
import { Response } from 'src/app/interfaces/general/response';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl: string;
    private productPath = '/catalog/product';
    
    constructor(
      private http   : HttpClient,
      private config : ConfigService,
      private utils  : UtilsService
      ) 
      { 
        this.apiUrl = this.config.API_URL+this.productPath;
      }
  
      getProducts(productRequest : ProductCreateRequest): Observable<Response<ProductResponse>> {
        let params = new HttpParams();
        
        if (productRequest) 
        {
          
          if(productRequest.product.status)
          {
              params = params.set('status', productRequest.product.status);     
          }     

          params = params.set('page', productRequest.pageNumber);     
          params = params.set('pageSize', productRequest.pageSize);  
          if(productRequest.product.categoryId)
          {
              params = params.set('categoryId', productRequest.product.categoryId);
          }
          if(productRequest.product.providerName)
          {
              params = params.set('providerName', productRequest.product.providerName);
          }
          if(productRequest.product.name)
          {
              params = params.set('description', productRequest.product.name);
          }
        }
        return this.http.get<Response<ProductResponse>>(this.apiUrl, { params: params }).pipe(
          catchError(this.utils.handleError)
        );
      }

      createProduct(product: Product): Observable<Response<Product[]>> {
        return this.http.post<Response<Product[]>>(this.apiUrl, product).pipe(
          catchError(this.utils.handleError)
        );
      }
  
      updateProduct(id: number, product: Product): Observable<Response<Product[]>> {
        return this.http.patch<Response<Product[]>>(`${this.apiUrl}/${id}`, product).pipe(
          catchError(this.utils.handleError)
        );
      }

      deleteProduct(id: number): Observable<Response<Product[]>> {
        return this.http.delete<Response<Product[]>>(`${this.apiUrl}/${id}`).pipe(
          catchError(this.utils.handleError)
        );
      }

      createProductCharacteristic(productId: number, productCharacteristic: ProductCharacteristic): Observable<Response<any>> {
        return this.http.post<Response<any>>(`${this.apiUrl}/${productId}/characteristic`, productCharacteristic).pipe(
          catchError(this.utils.handleError)
        );
      }

      uploadCharacteristicFile(characteristicId: string, file: File, type: string = 'IMAGE'): Observable<Response<any>> {
          const formData = new FormData();
          formData.append('file', file);
          formData.append('type', type);

          return this.http.post<Response<any>>(
            `${this.apiUrl}/characteristic/${characteristicId}/upload`,formData).pipe(
            catchError(this.utils.handleError)
          );
      }

      createProductPrices(productId: number, prices: ProductDiscountPrice[]): Observable<Response<any>> {
        return this.http.post<Response<any>>(`${this.apiUrl}/${productId}/price`, prices).pipe(
          catchError(this.utils.handleError)
        );
      }

      createProductDiscount(productId: number, prices: ProductDiscountPrice[]): Observable<Response<any>> {
        return this.http.post<Response<any>>(`${this.apiUrl}/${productId}/discount`, prices).pipe(
          catchError(this.utils.handleError)
        );
      }

      updateProductInventory(productId: number, inventoryData: ProductStock): Observable<Response<any>> {
        return this.http.put<Response<any>>(`${this.apiUrl}/${productId}/inventory`,inventoryData).pipe(
          catchError(this.utils.handleError)
        );
      }

      executeHateoasLink(link: string): Observable<Response<any>> {
        return this.http.get<Response<any>>(link).pipe(
          catchError(this.utils.handleError)
        );
      }
}
