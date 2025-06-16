import {Component, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import {AuthService} from './core/services/auth/auth.service';
import {Navbar} from './shared/components/navbar/navbar';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, Navbar],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  standalone: true
})
export class App implements OnInit {

  constructor(private authService: AuthService) {}

  async ngOnInit(): Promise<void> {
    await this.authService.init();
    console.log('AuthService initialized and login attempt made.');
  }

  protected title = 'frontend';
}
