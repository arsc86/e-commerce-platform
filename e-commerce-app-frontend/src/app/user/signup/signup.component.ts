import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/interfaces/users/user';
import { SignupService } from './signup.service';
import { Response } from 'src/app/interfaces/general/response';
import { HttpStatusCode } from '@angular/common/http';
import { SharedMessageService } from 'src/app/utils/shared-message-service';
import { Constants } from 'src/app/interfaces/general/constants';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone: false,
  templateUrl: './signup.component.html'
})
export class SignupComponent implements OnInit {

  isLoading : boolean = false;

  user: User = {
    firstName: '',
    lastName: '',
    username: '',
    profile: {
      email: ''
    },
    password: ''
  };

  response : Response<any>;

  constructor(
    private signUpService        : SignupService,
    private sharedMessageService : SharedMessageService,
     private router              : Router,
  ){}

  ngOnInit(): void {
   
  }

  validateUser(user: User): boolean {
    return user.firstName !== '' && 
           user.lastName !== '' && 
           user.username !== '' && 
           user.profile.email !== '' && 
           user.password !== '';
  }

  signUp(): void{    
    if (Object.keys(this.user).length == 0) 
    {
      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error', 'All fields are required');
      return;
    }
    if (!this.validateUser(this.user)) {
      this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error', 'All fields are required');
      return;
    }

    this.isLoading = true;
    this.signUpService.signup(this.user).subscribe({
        next: (data) => {
            this.response = data;         
            this.isLoading  = false; 
            if (this.response.code == HttpStatusCode.Created && this.response.status === 'OK') {
              this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.INFO, 'Sign-up', this.response.message);            
              this.router.navigate([Constants.ROUTING_PATHS.LOGIN]);
            }
        },
        error: (error) => {    
            this.isLoading = false;                  
            this.sharedMessageService.addMessage(Constants.MESSAGE_TYPES.ERROR, 'Error creating user', error);            
        }
    });
  }


}
