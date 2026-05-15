import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { ComplementoComponent } from './pages/complemento/complemento.component';
import { EnumUsuario } from './models/usuario/EnumUsuario';
import { authGuard } from './guards/auth-guard.guard';
import { ClienteDashboardComponent } from './pages/client/cliente-dashboard/cliente-dashboard.component';
import { FreelancerDashboardComponent } from './pages/freelancer/freelancer-dashboard/freelancer-dashboard.component';
import { AdminDashboardComponent } from './pages/admin/admin-dashboard//admin-dashboard.component';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AdminHabilidadesComponent } from './pages/admin/admin-habilidades/admin-habilidades.component';
import { AdminCategoriasComponent } from './pages/admin/admin-categorias/admin-categorias.component';
import { AdminUsuariosComponent } from './pages/admin/admin-usuarios/admin-usuarios.component';
import { MiPerfilComponent } from './pages/mi-perfil/mi-perfil.component';
import { FreelancerHabilidadesComponent } from './pages/freelancer/freelancer-habilidades/freelancer-habilidades.component';
import { MiCarteraComponent } from './pages/mi-cartera/mi-cartera.component';
import { ClienteProyectosComponent } from './pages/client/cliente-proyectos/cliente-proyectos.component';
import { FreelancerBuscarProyectosComponent } from './pages/freelancer/freelancer-buscar-proyectos/freelancer-buscar-proyectos.component';
import { FreelancerPropuestasComponent } from './pages/freelancer/freelancer-propuestas/freelancer-propuestas.component';
import { ClienteProyectoDetalleComponent } from './pages/client/cliente-proyecto-detalle/cliente-proyecto-detalle.component';
import { FreelancerContratosComponent } from './pages/freelancer/freelancer-contratos/freelancer-contratos.component';
import { FreelancerContratoDetalleComponent } from './pages/freelancer/freelancer-contrato-detalle/freelancer-contrato-detalle.component';
import { ClienteContratosComponent } from './pages/client/cliente-contratos/cliente-contratos.component';
import { ClienteContratoDetalleComponent } from './pages/client/cliente-contrato-detalle/cliente-contrato-detalle.component';
import { MisSolicitudesComponent } from './pages/mis-solicitudes/mis-solicitudes.component';
import { AdminSolicitudesComponent } from './pages/admin/admin-solicitudes/admin-solicitudes.component';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
        component: RegisterComponent
    },
    {
        path: 'complemento',
        component: ComplementoComponent,
        canActivate: [authGuard],
        data: { role: [EnumUsuario.Cliente, EnumUsuario.Freelancer] }
    },
    {
        path: '',
        component: MainLayoutComponent,
        children: [
            {
                path: 'cliente/dashboard',
                component: ClienteDashboardComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Cliente }
            },
            {
                path: 'freelancer/dashboard',
                component: FreelancerDashboardComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Freelancer }
            },
            {
                path: 'freelancer/habilidades',
                component: FreelancerHabilidadesComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Freelancer }
            },
            {
                path: 'freelancer/buscar-proyectos',
                component: FreelancerBuscarProyectosComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Freelancer }
            },
            {
                path: 'freelancer/propuestas',
                component: FreelancerPropuestasComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Freelancer }
            },
            {
                path: 'freelancer/contratos',
                component: FreelancerContratosComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Freelancer }
            },
            {
                path: 'freelancer/contrato-detalle',
                component: FreelancerContratoDetalleComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Freelancer }
            },
            {
                path: 'cliente/proyectos',
                component: ClienteProyectosComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Cliente }
            },
            {
                path: 'cliente/proyecto-detalle',
                component: ClienteProyectoDetalleComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Cliente }
            },
            {
                path: 'cliente/contratos',
                component: ClienteContratosComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Cliente }
            },
            {
                path: 'cliente/contrato-detalle',
                component: ClienteContratoDetalleComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Cliente }
            },
            {
                path: 'admin/dashboard',
                component: AdminDashboardComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Administrador }
            },
            {
                path: 'admin/habilidades',
                component: AdminHabilidadesComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Administrador }
            },
            {
                path: 'admin/categorias',
                component: AdminCategoriasComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Administrador }
            },
            {
                path: 'admin/usuarios',
                component: AdminUsuariosComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Administrador }
            },
            {
                path: 'admin/solicitudes',
                component: AdminSolicitudesComponent,
                canActivate: [authGuard],
                data: { role: EnumUsuario.Administrador }
            },
            {
                path: 'mi-perfil',
                component: MiPerfilComponent,
                canActivate: [authGuard],
                data: { role: [EnumUsuario.Cliente, EnumUsuario.Freelancer, EnumUsuario.Administrador] }
            },
            {
                path: 'mi-cartera',
                component: MiCarteraComponent,
                canActivate: [authGuard],
                data: { role: [EnumUsuario.Cliente, EnumUsuario.Freelancer, EnumUsuario.Administrador] }
            },
            {
                path: 'solicitudes',
                component: MisSolicitudesComponent,
                canActivate: [authGuard],
                data: { role: [EnumUsuario.Cliente, EnumUsuario.Freelancer] }
            }
        ]
    },
    {
        path: '**',
        redirectTo: 'login'
    }
];
