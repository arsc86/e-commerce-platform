import { Component, OnInit } from '@angular/core';
import { PrimeNGConfig } from 'primeng/api';
import { UserUtilService } from './interfaces/users/user-util.service';
import { Constants } from './interfaces/general/constants';
import { ConfigService } from './utils/config.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

    sessionedUserData: any;
    constructor(
        private primengConfig: PrimeNGConfig,
        private userUtilService : UserUtilService,
        private configService   : ConfigService,
    ) { }

    ngOnInit() {
        this.loadGoogleMapsApi();
        this.primengConfig.ripple = true;  
        let session = sessionStorage.getItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT);
        //console.log('Session valid:', session);
        if(session)
          this.loadUserProfile();    
    }

    loadUserProfile(): void {
        this.sessionedUserData = localStorage.getItem('_user_data');
        if(this.sessionedUserData) {
            this.sessionedUserData = JSON.parse(this.sessionedUserData);
            this.userUtilService.setUser(this.sessionedUserData);
        } else {
            this.userUtilService.setUser(null);
        }
    }

    loadGoogleMapsApi(): void {
      const apiKey = this.configService.GOOGLE_API_KEY;
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}`;
      document.head.appendChild(script);
    }
}
