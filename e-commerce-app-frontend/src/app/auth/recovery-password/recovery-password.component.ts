import { Component, OnInit } from '@angular/core';
import { Login } from 'src/app/interfaces/users/login';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { Constants } from 'src/app/interfaces/general/constants';
import { Response } from 'src/app/interfaces/general/response';
import { ActivatedRoute, Router } from '@angular/router';
import { RecoveryPasswordService } from './recovery-password.service';
import { HttpStatusCode } from '@angular/common/http';

@Component({
    selector: 'app-login',
    templateUrl: './recovery-password.component.html',
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
export class RecoveryPasswordComponent implements OnInit{
   
    password: string;
    newPassword: string;
    username: string;

    response : Response<Login>;

    isLoading: boolean = false;

    userRecovery: any;

    recoveryMailSent: boolean = false;

    isVerifiedAccount    : boolean = false;

    isPasswordChnaged    : boolean = false;

    token: string;

    constructor(
        public layoutService        : LayoutService,
        private recoveryPasswordService: RecoveryPasswordService,
        private router              : Router,
        private route               : ActivatedRoute,
        private sharedMessageService: SharedMessageService
    ) { }

    ngOnInit(): void {

        this.route.params.subscribe(params => {
            this.token = params['token'];

            if(this.token)
            {
                this.recoveryPasswordService.validateVerificationTolken(this.token).subscribe({ 
                    next: (data) => {
                        this.response = data;                                 
                        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                            this.isVerifiedAccount = true;     
                            this.sharedMessageService.addMessage(
                                Constants.MESSAGE_TYPES.SUCCESS,
                                'Recovery Password validation',
                                'Token validated successfully, you can now change your password'
                                );                      
                        }                       
                    },
                    error: (error) => {                                               
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error validating recovery token', 'Error validating token, please try again');
                    }
                });
            }            
        });
    }

    sendRecoveryEmail(){
         
        if(this.username == null || this.username == ''){
            this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.ERROR, 
                'Error', 
                'Username is required');            
        }
        else
        {
            this.userRecovery = {
                username: this.username
            };

            this.isLoading = true;
            this.recoveryPasswordService.sendRecoveryEmail(this.userRecovery).subscribe({
                next: (data) => 
                {
                    this.response = data;
                    if (this.response.code == HttpStatusCode.Accepted && this.response.status === 'OK') 
                    {                 
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Recovery Password', this.response.message);                            
                        this.recoveryMailSent = true;           
                    }
                    else 
                    {
                        this.sharedMessageService.addMessage(
                            Constants.MESSAGE_TYPES.ERROR,
                            'Recovery error',
                            this.response.message
                            );
                    }                    
                },
                error: (error) =>
                {
                    this.isLoading = false;
                    this.sharedMessageService.addMessage(
                        Constants.MESSAGE_TYPES.ERROR, 
                        'Recovery error', 
                        error);                   
                },
                complete: () => {
                    this.isLoading = false;
                }
            });
        }
    }

    changePassword() {
        
        if (!this.isVerifiedAccount) {
            this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.ERROR,
                'Error',
                'Account is not verified'
            );
            return;
        }

        if (this.token == null || this.token == '') {
            this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.ERROR,
                'Error',
                'The reference token is required'
            );
            return;
        }

        if (this.password == null || this.password == '' || this.newPassword == null || this.newPassword == '') {
            this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.ERROR,
                'Error changing password',
                'Both password fields are required'
            );
            return;
        }

        if (this.password !== this.newPassword) {
            this.sharedMessageService.addMessage(
                Constants.MESSAGE_TYPES.ERROR,
                'Error Changing password',
                'Passwords do not match'
            );
            return;
        }

        let newPassword = {
            newPassword: this.password,
            oldPassword: this.newPassword,
            token      : this.token
        }
        this.isLoading = true;
        this.recoveryPasswordService.chnagePassword(newPassword,'recovery').subscribe({
            next: (data) => {
                this.response = data;
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.isPasswordChnaged = true;
                    this.sharedMessageService.addMessage(
                        Constants.MESSAGE_TYPES.SUCCESS,
                        'Password Changed',
                        'Your password has been successfully changed'
                    );                   
                } else {
                    this.sharedMessageService.addMessage(
                        Constants.MESSAGE_TYPES.ERROR,
                        'Change Password Error',
                        this.response.message
                    );
                }
            },
            error: (error) => {
                this.isLoading = false;
                this.sharedMessageService.addMessage(
                    Constants.MESSAGE_TYPES.ERROR,
                    'Change Password Error',
                    error
                );
            },
            complete: () => {
                this.isLoading = false;
            }
        });
    }
    
}
