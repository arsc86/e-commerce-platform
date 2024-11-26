import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginRoutingModule } from './login-routing.module';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@NgModule({
    imports: [
        CommonModule,
        LoginRoutingModule,
        ButtonModule,      
        InputTextModule
    ],
    declarations: []
})
export class LoginModule { }
