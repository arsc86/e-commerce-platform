import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';
import { Response } from 'src/app/interfaces/general/response';
import { Region } from 'src/app/interfaces/catalog/region';

@Injectable({
  providedIn: 'root'
})
export class RegionService {

  private apiUrl: string;
  private rolePath = '/catalog/region';

  constructor(
      private http   : HttpClient,
      private config : ConfigService,
      private utils  : UtilsService
  ) { 
    this.apiUrl = this.config.API_URL+this.rolePath;
  }

  getRegions(region : Region): Observable<Response<Region[]>> {
    let params = new HttpParams();
        if (region) 
        {
          if(region.status)
          {
            params = params.set('status', region.status);
          }     
        }
        return this.http.get<Response<Region[]>>(this.apiUrl, { params: params }).pipe(
          catchError(this.utils.handleError)
        );
  }

  createRegion(region: Region): Observable<Response<Region[]>> {   
    return this.http.post<Response<Region[]>>(this.apiUrl, region).pipe(
      catchError(this.utils.handleError)
    );
  }

  updateRegion(id: string, region: any): Observable<Response<Region[]>> {
    return this.http.put<Response<Region[]>>(`${this.apiUrl}/${id}`, region).pipe(
          catchError(this.utils.handleError)
        );
  }

  deleteRegion(id: string): Observable<Response<Region[]>> {
    return this.http.delete<Response<Region[]>>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.utils.handleError)
    );
  }
}