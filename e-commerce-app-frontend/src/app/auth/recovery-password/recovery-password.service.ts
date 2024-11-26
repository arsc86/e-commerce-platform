import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class RecoveryPasswordService {

  private apiUrl: string;
  private apiVerifyTokenUrl: string;
  private apiChangePasswordUrl: string;
  private apiChangePassword: string;
  private recoveryPasswordEmail = '/user/recovery-password';
  private emailVerificationTokenPath = '/user/recovery-password/token';
  private changePasswordPath = '/user/recovery-password/change';
  private changePassword = '/user/change-password';

  constructor(
    private http: HttpClient,
    private config: ConfigService,
    private utils: UtilsService
  ) {
    this.apiUrl = this.config.API_URL + this.recoveryPasswordEmail;
    this.apiVerifyTokenUrl = this.config.API_URL + this.emailVerificationTokenPath;
    this.apiChangePasswordUrl = this.config.API_URL + this.changePasswordPath;
    this.apiChangePassword = this.config.API_URL + this.changePassword;
  }

  sendRecoveryEmail(userRecovery: any): Observable<Response<any>> {
    return this.http.post<Response<any>>(this.apiUrl, userRecovery).pipe(
      catchError(this.utils.handleError)
    );
  }

  validateVerificationTolken(token: string): Observable<Response<any>> {
    return this.http.get<Response<any>>(this.apiVerifyTokenUrl + '/' + token).pipe(
      catchError(this.utils.handleError)
    );
  }

chnagePassword(changePassword: any, type:string): Observable<Response<any>> {
  let url = '';
    if(type === 'recovery'){
      url = this.apiChangePasswordUrl;
    }else//change
    {
      url = this.apiChangePassword;     
    }

    return this.http.put<Response<any>>(url,changePassword).pipe(
      catchError(this.utils.handleError)
    );
  }

}
