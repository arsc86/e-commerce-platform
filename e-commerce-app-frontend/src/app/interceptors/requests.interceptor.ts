import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpStatusCode } from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { Login } from '../interfaces/users/login';
import { LoginService } from '../auth/login/login.service';
import { Constants } from '../interfaces/general/constants';
import { Router } from '@angular/router';
import { Response } from '../interfaces/general/response';
import Swal from 'sweetalert2';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    private isRefreshing = false;
    response : Response<Login>;
    
    constructor(      
        private loginService: LoginService,
        private router      : Router
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (req.url.includes('/user/login')         || 
            req.url.includes('/user/refresh-token') ||
            req.url.includes('/user/logout')       
        ) 
        {
            return next.handle(this.addToken(req));
        }
  
        return next.handle(this.addToken(req)).pipe(
            catchError(error => {
                if (error instanceof HttpErrorResponse && 
                    error.status === HttpStatusCode.Unauthorized) 
                {
                    return this.handle401Error(req, next);
                } 
                else 
                {
                    return throwError(error);
                }
            })
        );
        
    }

    private addToken(req: HttpRequest<any>) {       
        const authReq = req.clone({
            withCredentials: true,//cookies
        });
        return authReq;
    }

    private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (!this.isRefreshing) {
            this.isRefreshing = true;
           
            return this.loginService.refreshToken().pipe(
                switchMap((data) => {
                    this.response = data;
                    if (this.response.status === 'OK') 
                    {                                                                         
                        sessionStorage.setItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT, this.response.payload._val.toString());
                    } 
                    else {       
                        Swal.fire({
                            icon: 'warning',
                            title: 'Session Expired',
                            text: 'Your session has expired. Please login again',
                            confirmButtonText: 'OK'
                        }).then(() => {
                            this.isRefreshing = false;      
                            this.logout();   
                        });                                                       
                    }   
                    this.isRefreshing = false;
                    return next.handle(this.addToken(req));                             
                }),
                catchError((err) => {                        
                    Swal.fire({
                        icon: 'warning',
                        title: 'Session Expired',
                        text: 'Your session has expired. Please login again',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        this.isRefreshing = false;                           
                        this.logout();   
                    });    
                    return next.handle(this.addToken(req)); 
                })
            );
        } else {
            return throwError('No refresh token available');
        }
    }

    private logout() {
        sessionStorage.removeItem(Constants.SESSION_VAL_TYPES.USER_DATA);
        sessionStorage.removeItem(Constants.SESSION_VAL_TYPES.IS_VALID_ACCOUNT);
        this.loginService.logout().subscribe({
            next: (data) => {
                this.response = data;
                
                if (this.response.status === 'OK') 
                {                                                       
                    this.router.navigate(['/']);
                }
            },
            error: (error) => {
                Swal.fire({
                    icon: 'error',
                    title: 'Logout Error',
                    text: 'Error in logout',
                    confirmButtonText: 'OK'
                });                
            }
        });
        window.location.reload();
    }
}
