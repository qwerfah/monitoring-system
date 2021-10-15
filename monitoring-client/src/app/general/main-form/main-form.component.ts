import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TimeoutError } from 'rxjs';
import { UserDependentComponent } from 'src/app/helpers/user-dependent.component';

import { UserWithToken } from 'src/app/models/user';
import { UserRole } from 'src/app/models/user-role';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-main-form',
  templateUrl: './main-form.component.html',
  styleUrls: ['./main-form.component.css'],
})
export class MainFormComponent extends UserDependentComponent implements OnInit {
  isVisible: boolean = true;

  constructor(sessionService: SessionService, private router: Router) {
    super(sessionService);
  }

  ngOnInit() {}

  onClickedOutside(e: Event): void {
    console.log((<HTMLButtonElement>event?.srcElement)?.attributes?.getNamedItem('id')?.value);
    this.isVisible =
      (<HTMLElement>event?.srcElement)?.attributes?.getNamedItem('id')?.value == 'sideBarButton' ? true : false;
  }

  onClicked(e: Event): void {
    this.isVisible = !this.isVisible;
  }

  logout(): void {
    if (confirm('Вы уверены, что хотите выйти?')) {
      this.sessionService.logout();
      this.router.navigate(['/login']);
    }
  }
}
