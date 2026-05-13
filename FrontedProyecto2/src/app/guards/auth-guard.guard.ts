import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';
import { inject } from '@angular/core';
import { EnumUsuario } from '../models/usuario/EnumUsuario';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const expectedRole = route.data['role'] as string[]; 
  const currentUser = authService.currentUser();

  if (!authService.getToken() || !currentUser) {
    router.navigate(['/login']);
    return false;
  }

  if (currentUser.rol && expectedRole.includes(currentUser.rol)) {
    return true;
  }

  console.warn(`Acceso denegado. Rol: ${currentUser.rol}. Requeridos: ${expectedRole}`);

  if (currentUser.rol === EnumUsuario.Administrador) router.navigate(['/admin/dashboard']);
  else if (currentUser.rol === EnumUsuario.Cliente) router.navigate(['/cliente/dashboard']);
  else router.navigate(['/freelancer/dashboard']);
  return false;
}
