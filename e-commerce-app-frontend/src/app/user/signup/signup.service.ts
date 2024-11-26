import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { User } from 'src/app/interfaces/users/user';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class SignupService {

  private apiUrl : string;
  private signupPath = "/user";

  constructor(
    private http: HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
  ) { 
    this.apiUrl = this.config.API_URL + this.signupPath;
  }

  signup(user: User): Observable<any> {
    return this.http.post<Response<any>>(this.apiUrl, user).pipe(
      catchError(this.utils.handleError)
    );
  }
}
