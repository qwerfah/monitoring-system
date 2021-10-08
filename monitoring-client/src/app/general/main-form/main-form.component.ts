import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TimeoutError } from 'rxjs';

import { User } from 'src/app/models/user';
import { UserRole } from 'src/app/models/user-role';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-main-form',
  templateUrl: './main-form.component.html',
  styleUrls: ['./main-form.component.css'],
})
export class MainFormComponent implements OnInit {
  isVisible: boolean = true;
  currentUser: User | undefined = undefined;

  UserRole = UserRole;

  constructor(private sessionService: SessionService, private router: Router) {
    sessionService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
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

  /** Check if current logged in user has sufficient rights to access element.
   * @param roles Array of sufficient user roles.
   * @returns True if current user role is sufficient, otherwise false.
   */
  isAllowed(roles: UserRole[]): boolean {
    return this.currentUser !== undefined && roles.indexOf(this.currentUser.role) !== -1;
  }

  logout(): void {
    this.sessionService.logout();
    this.router.navigate(['/login']);
  }
}
