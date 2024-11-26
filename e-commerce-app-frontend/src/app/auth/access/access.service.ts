import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class AccessService {

  private apiUrl: string;
  private apiVerifyTokenUrl: string;
  private emailVerificationPath      = '/user/validate/email';
  private emailVerificationTokenPath = '/user/validate/email/token';

  constructor(
    private http : HttpClient,
    private config: ConfigService,
    private utils : UtilsService
  ) 
  { 
    this.apiUrl = this.config.API_URL + this.emailVerificationPath;
    this.apiVerifyTokenUrl = this.config.API_URL + this.emailVerificationTokenPath;
  }

  sendVerificationEmail(email: any): Observable<Response<any>> {
    return this.http.post<Response<any>>(this.apiUrl,email).pipe(
          catchError(this.utils.handleError)
        );
  }

  validateVerificationTolken(token: string): Observable<Response<any>> {
    return this.http.get<Response<any>>(this.apiVerifyTokenUrl+'/'+token).pipe(
          catchError(this.utils.handleError)
        );
  }
}
