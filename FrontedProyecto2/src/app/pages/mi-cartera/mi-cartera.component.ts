import { Component, inject, OnInit, signal } from '@angular/core';
import { UsuarioService } from '../../services/usuario/usuario.service';
import { AuthService } from '../../services/auth/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mi-cartera.component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './mi-cartera.component.html'
})
export class MiCarteraComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private authService = inject(AuthService);
  private fb = inject(FormBuilder);

  enumUsuario = EnumUsuario;
  rolActual = this.authService.currentUser()?.rol;
  
  transacciones = signal<any[]>([]);
  isLoading = signal<boolean>(false);
  successMessage = signal<string>('');
  errorMessage = signal<string>('');

  recargaForm: FormGroup = this.fb.group({
    monto: ['', [Validators.required, Validators.min(1)]]
  });

  ngOnInit() {
    this.cargarTransacciones();
  }

  cargarTransacciones() {
    this.usuarioService.getTransacciones().subscribe({
      next: (res) => this.transacciones.set(res)
    });
  }

  realizarRecarga() {
    if (this.recargaForm.invalid) {
      this.recargaForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.successMessage.set('');
    this.errorMessage.set('');

    this.usuarioService.recargarCartera(this.recargaForm.value).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.successMessage.set('Recarga exitosa. El saldo ya está en tu cartera.');
        this.recargaForm.reset();
        this.cargarTransacciones();
        this.usuarioService.refrescarCarteraGlobal();
        setTimeout(() => this.successMessage.set(''), 3000);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al recargar saldo.');
        setTimeout(() => this.errorMessage.set(''), 3000);
      }
    });
  }
}