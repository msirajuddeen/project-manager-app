import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProjectComponent } from './pages/project/project.component';
import { UserComponent } from './pages/user/user.component';
import { AddTaskComponent } from './pages/task/add-task/add-task.component';
import { ViewTaskComponent } from './pages/task/view-task/view-task.component';
import { EditTaskComponent } from './pages/task/edit-task/edit-task.component';
import { ProjectManagerService } from './shared/project-manager-service';
import { SortFilterPipe } from './shared/sort-pipe';
import { SearchFilter } from './shared/search-filter';
import { DatePipe } from '@angular/common';
@NgModule({
  declarations: [
    AppComponent,
    ProjectComponent,
    UserComponent,
    AddTaskComponent,
    ViewTaskComponent,
    EditTaskComponent,
    SortFilterPipe,
    SearchFilter
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [ProjectManagerService,DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
