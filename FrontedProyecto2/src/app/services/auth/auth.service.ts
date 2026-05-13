import { inject, Injectable, signal } from '@angular/core';
import { RestConstants } from '../../shared/restapi/rest-constants';
import { HttpClient } from '@angular/common/http';
import { LoginRequest } from '../../models/usuario/login/LoginRequest';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from '../../models/usuario/login/LoginResponse';
import { UsuarioResponse } from '../../models/usuario/UsuarioResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private restConstants = new RestConstants();
  private http = inject(HttpClient);
  private apiUrl = `${this.restConstants.getApiUrl()}auth`;

  currentUser = signal<UsuarioResponse | null>(this.loadUserFromStorage());

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('usuario', JSON.stringify(response.usuario));
        this.currentUser.set(response.usuario);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    this.currentUser.set(null);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  private loadUserFromStorage(): UsuarioResponse | null {
    const userStr = localStorage.getItem('usuario');
    return userStr ? JSON.parse(userStr) : null;
  }
}
