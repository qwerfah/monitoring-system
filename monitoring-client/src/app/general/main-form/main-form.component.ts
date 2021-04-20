import { Component, OnInit } from '@angular/core';
import { TimeoutError } from 'rxjs';

@Component({
  selector: 'app-main-form',
  templateUrl: './main-form.component.html',
  styleUrls: ['./main-form.component.css'],
})
export class MainFormComponent implements OnInit {
  isVisible: boolean = true;
  events: string[] = [];
  opened: boolean = true;

  constructor() {}

  ngOnInit() {}

  onClickedOutside(e: Event): void {
    console.log(
      (<HTMLButtonElement>event?.srcElement)?.attributes?.getNamedItem('id')
        ?.value
    );
    this.isVisible =
      (<HTMLElement>event?.srcElement)?.attributes?.getNamedItem('id')?.value ==
      'sideBarButton'
        ? true
        : false;
    console.log('Clicked outside:', e);
  }

  onClicked(e: Event): void {
    this.isVisible = !this.isVisible;
    console.log('Clicked:', e);
  }
}
