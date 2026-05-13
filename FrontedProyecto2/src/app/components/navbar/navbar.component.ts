import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';
import { UsuarioService } from '../../services/usuario/usuario.service';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-navbar-component',
  imports: [RouterLink, RouterLinkActive, DecimalPipe],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private usuarioService = inject(UsuarioService);
  
  enumUsuario = EnumUsuario;
  cartera = this.usuarioService.carteraActual;

  usuarioActual = this.authService.currentUser;
  nombreUsuario = computed(() => this.usuarioActual()?.nombreCompleto || 'Usuario');
  rolUsuario = computed(() => this.usuarioActual()?.rol as EnumUsuario);

  ngOnInit() {
    if (this.usuarioActual()) {
      this.usuarioService.refrescarCarteraGlobal();
    }
  }

  rutaDashboard = computed(() => {
    switch (this.rolUsuario()) {
      case EnumUsuario.Cliente:
        return '/cliente/dashboard';
      case EnumUsuario.Freelancer:
        return '/freelancer/dashboard';
      case EnumUsuario.Administrador:
        return '/admin/dashboard';
      default:
        return '/';
    }
  });

  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
