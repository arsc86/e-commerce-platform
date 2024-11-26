import { Component } from '@angular/core';
import { Login } from 'src/app/interfaces/users/login';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { LoginService } from './login.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { Constants } from 'src/app/interfaces/general/constants';
import { Response } from 'src/app/interfaces/general/response';
import { Router } from '@angular/router';
import { UserService } from 'src/app/admin/users/user.service';
import { User } from 'src/app/interfaces/users/user';
import { HttpStatusCode } from '@angular/common/http';
import { UserUtilService } from 'src/app/interfaces/users/user-util.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styles: [`
        :host ::ng-deep .pi-eye,
        :host ::ng-deep .pi-eye-slash {
            transform:scale(1.6);
            margin-right: 1rem;
            color: var(--primary-color) !important;
        }
    `],
    standalone: false
})
export class LoginComponent {

    valCheck: string[] = ['remember'];

    public userLogin : Login;

    password: string;
    username: string;

    response : Response<Login>;

    isLoading: boolean = false;

    user : User;

    users: User[] = [];

    responseUser : Response<User[]>;

    constructor(
        public layoutService        : LayoutService,
        private loginService        : LoginService,
        private router              : Router,
        private sharedMessageService: SharedMessageService,
        private userService     : UserService,
        private userUtilService : UserUtilService
    ) { }

    login(){

        this.user = {
            
            username: this.username,
            password: this.password
        };

        this.isLoading = true;
        if(this.username == null || this.password == null){
            this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.ERROR, 
                'Error', 
                'Username and password are required');            
        }
        else
        {
            this.userLogin = {
                username: this.username,
                password: this.password
            };
        
            this.loginService.login(this.userLogin).subscribe({
                next: (data) => 
                {
                    this.response = data;                    
                    if (this.response.status === 'OK') 
                    {                                             
                        if(this.response.payload._exp)
                        {
                            //To change password
                            this.router.navigate([Constants.ROUTING_PATHS.CHANGE_PASS]);
                            //----------------
                        }
                        else
                        {
                            if(this.response.payload._val)
                            {                                       
                                this.userService.getUsers(this.user).subscribe({
                                    next: (data) => {
                                        this.responseUser = data;
                                        this.userUtilService.setLocalStorageUserData(this.responseUser.payload[0]);
                                   
                                        sessionStorage.setItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT, 'true');
                                        this.router.navigate([Constants.ROUTING_PATHS.HOME]).then(() => {                                                                                        
                                            window.location.reload();
                                        });
                                    },
                                    error: (error) => {                     
                                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error getting users', error);            
                                    }
                                });                            
                            }
                            else
                            {
                                sessionStorage.setItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT, 'false');
                                this.router.navigate([Constants.ROUTING_PATHS.VALIDATE_EMAIL]);
                            }  
                        }                                              
                    } 
                    else 
                    {
                        this.sharedMessageService.addMessage(
                            Constants.MESSAGE_TYPES.ERROR,
                            'Authentication error',
                            this.response.message
                            );
                    }                    
                },
                error: (error) =>
                {
                    this.isLoading = false;
                    this.sharedMessageService.addMessage(
                        Constants.MESSAGE_TYPES.ERROR, 
                        'Authentication error', 
                        error);                   
                },
                complete: () => {
                    this.isLoading = false;
                }
            });
        }
    } 
}
