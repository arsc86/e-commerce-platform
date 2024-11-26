import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

    public API_URL          = environment.API_URL;
    public BASE_COORDINATES = environment.BASE_COORDINATES;
    public GOOGLE_API_KEY   = environment.GOOGLE_API_KEY;
    
    constructor() {}

    getApiUrl(): string {
        return environment.API_URL;
    }

    isProduction(): boolean {
        return environment.production;
    }
}