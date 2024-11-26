import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { LayoutService } from "./service/app.layout.service";
import { LoginService } from '../auth/login/login.service';
import { Router } from '@angular/router';
import { SharedMessageService } from '../utils/shared-message-service';
import { Constants } from '../interfaces/general/constants';
import { Response } from '../interfaces/general/response';
import { MenuItem } from 'primeng/api';
import { UserUtilService } from '../interfaces/users/user-util.service';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html',
    standalone: false
})
export class AppTopBarComponent implements OnInit{

    @ViewChild('menubutton') menuButton!: ElementRef;

    @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

    @ViewChild('topbarmenu') menu!: ElementRef;

    sessionedUser: boolean = false;

    response : Response<any>;

    sessionedUserData: any;

    breadcrumbItems: MenuItem[] = [];

    constructor(
      public layoutService: LayoutService,
      private loginService        : LoginService,
      private router              : Router,     
      private sharedMessageService: SharedMessageService,
      private userUtilService     : UserUtilService,
    ) { }

    ngOnInit(): void {        

        this.sessionedUserData = localStorage.getItem('_user_data');
        if(this.sessionedUserData) {
            this.sessionedUserData = JSON.parse(this.sessionedUserData);
            this.sessionedUser = true;
            this.breadcrumbItems = [];
            this.breadcrumbItems.push({
                label: this.sessionedUserData.firstName + ' ' + this.sessionedUserData.lastName
            });
        } else {
            this.sessionedUser = false;
            this.breadcrumbItems = [];
        }
    }

    logout(){
      this.sessionedUser   = false;                            
      sessionStorage.removeItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT);   
      localStorage.removeItem('_user_data'); // Clear user data from local storage
      this.loginService.logout().subscribe({
          next: () => 
          {                                             
              this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.SUCCESS, 
                'Logout', 
                'You have been logged out successfully');  
                              
              this.router.navigate(['/']).then(() => {
                  window.location.reload();
              });               
          },
          error: (error) =>
          {             
              this.sharedMessageService.addMessage(
                  Constants.MESSAGE_TYPES.WARN, 
                  'Logout error', 
                  error);                   
          }
      });
    }    
}
