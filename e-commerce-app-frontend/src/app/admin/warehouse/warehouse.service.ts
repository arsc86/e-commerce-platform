import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';
import { Response } from 'src/app/interfaces/general/response';
import { Warehouse } from 'src/app/interfaces/catalog/warehouse';

@Injectable({
  providedIn: 'root'
})
export class WarehouseService {

  private apiUrl: string;
    private warehousePath = '/catalog/warehouse';
    
      constructor(
          private http   : HttpClient,
          private config : ConfigService,
          private utils  : UtilsService
      ) { 
        this.apiUrl = this.config.API_URL+this.warehousePath;
      }
    
      getWarehouses(warehouse : Warehouse): Observable<Response<Warehouse[]>> {
        let params = new HttpParams();
            if (warehouse) 
            {
              if(warehouse.status)
              {
                params = params.set('status', warehouse.status);
              }     
            }
            return this.http.get<Response<Warehouse[]>>(this.apiUrl, { params: params }).pipe(
              catchError(this.utils.handleError)
            );
      }
    
      createWarehouse(warehouse: Warehouse): Observable<Response<Warehouse[]>> {   
        return this.http.post<Response<Warehouse[]>>(this.apiUrl, warehouse).pipe(
          catchError(this.utils.handleError)
        );
      }
    
      updateWarehouse(id: string, warehouse: any): Observable<Response<Warehouse[]>> {
        return this.http.put<Response<Warehouse[]>>(`${this.apiUrl}/${id}`, warehouse).pipe(
              catchError(this.utils.handleError)
            );
      }
    
      deleteWarehouse(id: string): Observable<Response<Warehouse[]>> {
        return this.http.delete<Response<Warehouse[]>>(`${this.apiUrl}/${id}`).pipe(
          catchError(this.utils.handleError)
        );
      }
}
