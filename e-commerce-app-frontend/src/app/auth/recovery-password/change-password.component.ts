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
    templateUrl: './change-password.component.html',
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
export class ChangePasswordComponent implements OnInit{
   
    password   : string;
    newPassword: string;
    username   : string;

    response : Response<Login>;

    isLoading: boolean = false;

    isPasswordChnaged    : boolean = false;

    constructor(
        public layoutService        : LayoutService,
        private recoveryPasswordService: RecoveryPasswordService,
        private router              : Router,
        private route               : ActivatedRoute,
        private sharedMessageService: SharedMessageService
    ) { }

    ngOnInit(): void {        
    }

    changePassword() {

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
        }
        this.isLoading = true;
        this.recoveryPasswordService.chnagePassword(newPassword,'change').subscribe({
            next: (data) => {
                this.response = data;
                if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                    this.isPasswordChnaged = true;
                    this.sharedMessageService.addMessage(
                        Constants.MESSAGE_TYPES.SUCCESS,
                        'Password Changed',
                        'Your password has been successfully changed'
                    );  
                    sessionStorage.removeItem(Constants.SESSION_VAL_TYPES.USER_DATA);  
                    sessionStorage.removeItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT);                  
                    this.router.navigate([Constants.ROUTING_PATHS.LOGIN]);
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
