import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { AccessRoutingModule } from './access-routing.module';
import { ChipModule } from 'primeng/chip';

@NgModule({
    imports: [
        CommonModule,
        AccessRoutingModule,
        ButtonModule
    ],
    declarations: []
})
export class AccessModule { }
