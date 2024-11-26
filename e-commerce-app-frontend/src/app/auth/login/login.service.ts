import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { aC } from '@fullcalendar/core/internal-common';
import { catchError, Observable } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { Login } from 'src/app/interfaces/users/login';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiLoginUrl : string;
  private apiLogoutUrl: string;
  private apiRefreshTokenUrl: string;
  private apiUserInfoUrl: string;

  private loginPath  = '/user/login';
  private logoutPath = '/user/logout';
  private refreshTokenPath = '/user/refresh-token';
  private userInfoPath = '/user/user-info';

  constructor(
    private http   : HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
  ) { 
      this.apiLoginUrl           = this.config.API_URL + this.loginPath;
      this.apiLogoutUrl          = this.config.API_URL + this.logoutPath;
      this.apiRefreshTokenUrl    = this.config.API_URL + this.refreshTokenPath;
      this.apiUserInfoUrl        = this.config.API_URL + this.userInfoPath;
  }

  login(userLogin : Login): Observable<Response<Login>> {
    return this.http.post<Response<Login>>(this.apiLoginUrl, userLogin).pipe(
      catchError(this.utils.handleError)
    );
  }   

  logout(): Observable<Response<Login>>{
    return this.http.get<Response<Login>>(this.apiLogoutUrl).pipe(
      catchError(this.utils.handleError)
    );
  }  

  refreshToken(): Observable<Response<Login>>{
    return this.http.get<Response<Login>>(this.apiRefreshTokenUrl,{ withCredentials: true }).pipe(
      catchError(this.utils.handleError)
    );
  }

  getUserInfo(): Observable<Response<Login>>{
    return this.http.get<Response<Login>>(this.apiUserInfoUrl,{ withCredentials: true }).pipe(
      catchError(this.utils.handleError)
    );
  }
}
