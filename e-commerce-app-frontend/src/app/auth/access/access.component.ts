import { Component, OnInit } from '@angular/core';
import { Constants } from 'src/app/interfaces/general/constants';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { UtilsService } from 'src/app/utils/utils.service';
import { AccessService } from './access.service';
import { Response } from 'src/app/interfaces/general/response';
import { HttpStatusCode } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
    selector: 'app-access',
    templateUrl: './access.component.html',
    standalone: false
})
export class AccessComponent implements OnInit { 

    email             : string = '';
    sessionedUserData : any;
    loginName         : string = '';
    //Response
    response : Response<any>;
    token    : string = '';

    isSessoinValid : boolean = false;

    constructor(
        private sharedMessageService: SharedMessageService,
        private utilService         : UtilsService,
        private accessService       : AccessService,
        private router              : Router
    ) { }

    ngOnInit(): void {

        this.sessionedUserData = this.utilService.getSessionStorageItem(Constants.SESSION_VAL_TYPES.USER_DATA);
        if(this.sessionedUserData){
            this.isSessoinValid = true;
            this.loginName = this.sessionedUserData.firstName;
        }
    }

    sendVerificationEmail(){
        if(this.email==""){
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error', 'Please enter your email');                        
        }
        else if (!this.isValidEmail(this.email)) {
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error', 'Please enter a valid email');
        }
        else{
            let email = {
                email : this.email
            }
            this.accessService.sendVerificationEmail(email).subscribe({
                next: (data) => {
                    this.response = data;                                 
                    if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.SUCCESS, 'Success', this.response.message);
                        this.router.navigate([Constants.ROUTING_PATHS.VERIFIED_EMAIL ]);
                    }
                },
                error: (error) => {                         
                    this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error sending email', error);            
                }
            });
        }
    }

    isValidEmail(email: string): boolean {
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(email);
    }
}
