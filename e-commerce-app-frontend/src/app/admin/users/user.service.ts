import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { Response } from 'src/app/interfaces/general/response';
import { User } from 'src/app/interfaces/users/user';
import { ConfigService } from 'src/app/utils/config.service';
import { UtilsService } from 'src/app/utils/utils.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl: string;
  private userPath = '/user';

  constructor(
    private http   : HttpClient,
    private config : ConfigService,
    private utils  : UtilsService
  ) 
  { 
    this.apiUrl = this.config.API_URL+this.userPath;
  }

  getUsers(user : User): Observable<Response<User[]>> {
    let params = new HttpParams();
    if (user) 
    {
      if(user.status)
      {
        params = params.set('status', user.status);     
      }     
      if(user.role)
      {
        params = params.set('role', user.role);
      }
      if(user.username)
      {
        params = params.set('username', user.username);
      }
    }
    return this.http.get<Response<User[]>>(this.apiUrl, { params: params }).pipe(
      catchError(this.utils.handleError)
    );
  }

  createUser(user: User): Observable<Response<User[]>> {    
    return this.http.post<Response<User[]>>(this.apiUrl, user).pipe(
      catchError(this.utils.handleError)
    );
  }
  
  updateUser(id: string, user: User): Observable<Response<User[]>> {
    return this.http.patch<Response<User[]>>(`${this.apiUrl}/${id}`, user).pipe(
      catchError(this.utils.handleError)
    );
  }
  
  deleteUser(id: string): Observable<Response<User[]>> {
    return this.http.delete<Response<User[]>>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.utils.handleError)
    );
  }

  

}
