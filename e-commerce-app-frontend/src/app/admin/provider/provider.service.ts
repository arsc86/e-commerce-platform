import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Provider } from 'src/app/interfaces/catalog/provider';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';
import { Response } from 'src/app/interfaces/general/response';

@Injectable({
  providedIn: 'root'
})
export class ProviderService {

  private apiUrl: string;
  private providerPath = '/catalog/provider';
  
    constructor(
        private http   : HttpClient,
        private config : ConfigService,
        private utils  : UtilsService
    ) { 
      this.apiUrl = this.config.API_URL+this.providerPath;
    }
  
    getProviders(provider : Provider): Observable<Response<Provider[]>> {
      let params = new HttpParams();
          if (provider) 
          {
            if(provider.status)
            {
              params = params.set('status', provider.status);
            }     
          }
          return this.http.get<Response<Provider[]>>(this.apiUrl, { params: params }).pipe(
            catchError(this.utils.handleError)
          );
    }
  
    createProvider(provider: Provider): Observable<Response<Provider[]>> {   
      return this.http.post<Response<Provider[]>>(this.apiUrl, provider).pipe(
        catchError(this.utils.handleError)
      );
    }
  
    updateProvider(id: string, provider: any): Observable<Response<Provider[]>> {
      return this.http.put<Response<Provider[]>>(`${this.apiUrl}/${id}`, provider).pipe(
            catchError(this.utils.handleError)
          );
    }
  
    deleteProvider(id: string): Observable<Response<Provider[]>> {
      return this.http.delete<Response<Provider[]>>(`${this.apiUrl}/${id}`).pipe(
        catchError(this.utils.handleError)
      );
    }
}
