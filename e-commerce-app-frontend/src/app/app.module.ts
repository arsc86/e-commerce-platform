import { NgModule } from '@angular/core';
import { HashLocationStrategy, LocationStrategy, PathLocationStrategy } from '@angular/common';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AppLayoutModule } from './layout/app.layout.module';
import { NotfoundComponent } from './demo/components/notfound/notfound.component';
import { BlockUIModule } from 'primeng/blockui'; // Adjust the import path as necessary
import { ProductService } from './demo/service/product.service';
import { CountryService } from './demo/service/country.service';
import { CustomerService } from './demo/service/customer.service';
import { EventService } from './demo/service/event.service';
import { IconService } from './demo/service/icon.service';
import { NodeService } from './demo/service/node.service';
import { PhotoService } from './demo/service/photo.service';
import { ToastModule } from 'primeng/toast';
import { SharedMessageService } from './utils/shared-message-service';
import { MessageService } from 'primeng/api';
import { CookieService } from 'ngx-cookie-service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/requests.interceptor';
import { AdminModule } from './admin/admin.module';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { UtilsService } from './utils/utils.service';
import { UserModule } from './user/user.module';
import { GoogleMapsModule } from '@angular/google-maps';

@NgModule({
    declarations: [AppComponent, NotfoundComponent],
    imports: [
        AppRoutingModule, 
        AppLayoutModule, 
        BlockUIModule, 
        ToastModule, 
        SweetAlert2Module.forRoot(),
        AdminModule,
        UserModule,
        GoogleMapsModule
    ],
    providers: [
        { provide: LocationStrategy, useClass: PathLocationStrategy },
        CountryService, 
        CustomerService, 
        EventService, 
        IconService, 
        NodeService,
        PhotoService, 
        ProductService, 
        SharedMessageService, 
        MessageService,
        CookieService,
        UtilsService,
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}
