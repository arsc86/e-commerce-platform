import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Response } from '../interfaces/general/response';
import { Region } from '../interfaces/catalog/region';
import { ConfigService } from '../utils/config.service';
import { UtilsService } from '../utils/utils.service';

@Injectable({
    providedIn: 'root'
  })
export class LocationService {

    regionPath   : string = '/catalog/region';
    apiUrlRegion : string;    

    constructor(
        private http   : HttpClient,
        private config : ConfigService,
        private utils  : UtilsService) 
    {
        this.apiUrlRegion = this.config.API_URL + this.regionPath;    
    }

    getCountries() {
        return this.http.get<any>('assets/demo/data/countries.json')
            .toPromise()
            .then(res => res.data as any[])
            .then(data => data);
    }

    getCities() {
        return this.http.get<any>('assets/demo/data/cities.json')
            .toPromise()
            .then(res => res.data as any[])
            .then(data => data);
    }

    getParentRegions() {
        return this.http.get<any>('assets/demo/data/regions.json')
            .toPromise()
            .then(res => res.data as any[])
            .then(data => data);
    }

    getRegion() : Observable<Response<Region[]>> {
        return this.http.get<Response<Region[]>>(this.apiUrlRegion).pipe(
              catchError(this.utils.handleError)
        );
    }
}
