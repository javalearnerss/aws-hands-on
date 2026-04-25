import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-list-user',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './list-user.component.html',
  styleUrl: './list-user.component.css'
})
export class ListUserComponent {
users:any[]=[];

  constructor(){}

  ngOnInit(){

    this.loadUsers();

  }

  loadUsers(){


  }

  viewUser(user:any){

    alert(JSON.stringify(user,null,2));

  }
}
