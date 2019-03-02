import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProjectComponent } from './pages/project/project.component';
import { UserComponent } from './pages/user/user.component';
import { AddTaskComponent } from './pages/task/add-task/add-task.component';
import { ViewTaskComponent } from './pages/task/view-task/view-task.component';
import { EditTaskComponent } from './pages/task/edit-task/edit-task.component';

const routes: Routes = [];

@NgModule({
    imports: [
    RouterModule.forRoot([
      { path: '', redirectTo: '/addUser', pathMatch: 'full' },
      { path: 'addUser', component: UserComponent },
      { path: 'addProject', component: ProjectComponent },
      { path: 'addTask', component: AddTaskComponent  },
      { path: 'editTask', component: EditTaskComponent },
      { path: 'viewTask', component: ViewTaskComponent  }
    ])
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
