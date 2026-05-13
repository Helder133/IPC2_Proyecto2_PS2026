import { Component } from '@angular/core';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-main-layout.component',
  imports: [NavbarComponent, RouterOutlet],
  templateUrl: './main-layout.component.html'
})
export class MainLayoutComponent {}
