import { Component, OnInit } from '@angular/core';
import { Constants } from 'src/app/interfaces/general/constants';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { AccessService } from './access.service';
import { Response } from 'src/app/interfaces/general/response';
import { HttpStatusCode } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-access',
    templateUrl: './access-verified.component.html',
    standalone: false
})
export class AccessVerifiedComponent implements OnInit { 

    email             : string = '';
    sessionedUserData : any;
    loginName         : string = '';
    //Response
    response : Response<any>;
    token    : string = '';

    //html messages
    isVerifiedAccount    : boolean = false;
    isVerificationFailed : boolean = false;

    constructor(
        private sharedMessageService: SharedMessageService,
        private accessService       : AccessService,
        private route               : ActivatedRoute
    ) { }

    ngOnInit(): void {

        this.isVerificationFailed = false;
       
        this.route.params.subscribe(params => {
            this.token = params['token'];

            if(this.token)
            {
                this.accessService.validateVerificationTolken(this.token).subscribe({ 
                    next: (data) => {
                        this.response = data;                                 
                        if (this.response.code == HttpStatusCode.Ok && this.response.status === 'OK') {
                            this.isVerifiedAccount = true;
                        }
                        else{
                            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.WARN, 'Validating token', this.response.message);
                            this.isVerifiedAccount = true; 
                        }
                    },
                    error: (error) => {   
                        this.isVerificationFailed = true;                      
                        this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error validating token', 'Error validating token, please try again');
                    }
                });
            }            
        });
    }
}
