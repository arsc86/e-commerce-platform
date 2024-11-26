import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError } from 'rxjs';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';
import { Response } from 'src/app/interfaces/general/response';
import { Characteristic } from 'src/app/interfaces/catalog/characteristic';

@Injectable({
  providedIn: 'root'
})
export class CharacteristicService {

  private apiUrl: string;
  private characteristicPath = '/catalog/characteristic';
  
  constructor(
    private http   : HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
    ) 
    { 
      this.apiUrl = this.config.API_URL+this.characteristicPath;
    }

    getCharacteristics(characteristic : Characteristic): Observable<Response<Characteristic[]>> {
      let params = new HttpParams();
      if (characteristic) 
      {
        if(characteristic.status)
        {
          params = params.set('status', characteristic.status);     
        }     
      
      }
      return this.http.get<Response<Characteristic[]>>(this.apiUrl, { params: params }).pipe(
        catchError(this.utils.handleError)
      );
    }

    createCharacteristic(characteristic: Characteristic): Observable<Response<Characteristic[]>> {
      return this.http.post<Response<Characteristic[]>>(this.apiUrl, characteristic).pipe(
        catchError(this.utils.handleError)
      );
    }

    updateCharacteristic(id: string, characteristic: Characteristic): Observable<Response<Characteristic[]>> {
      return this.http.patch<Response<Characteristic[]>>(`${this.apiUrl}/${id}`, characteristic).pipe(
        catchError(this.utils.handleError)
      );
    }

    deleteCharacteristic(id: string): Observable<Response<Characteristic[]>> {
      return this.http.delete<Response<Characteristic[]>>(`${this.apiUrl}/${id}`).pipe(
        catchError(this.utils.handleError)
      );
    }
}
