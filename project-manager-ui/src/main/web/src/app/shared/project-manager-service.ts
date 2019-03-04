import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Config } from './config/env.config';
@Injectable()
export class ProjectManagerService {
 constructor(private httpClient: HttpClient) {}   
     task: any = {};
     getHeader(){
        return new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept':'*/*'
         });
      }

    getUser() {
      return this.httpClient.get(Config.apiUrl+'getUser', {headers: this.getHeader()});
    }

    getProject() {
      return this.httpClient.get(Config.apiUrl+'getProject', {headers: this.getHeader()});
    }

    getParentTask() {
      return this.httpClient.get(Config.apiUrl+'getParentTask', {headers: this.getHeader()});
    }

    viewTask(inputParam : {}) {
      return this.httpClient.post(Config.apiUrl+'viewTask', inputParam, {headers: this.getHeader()});
    }

    updateUser(inputParam : {}) {
      return this.httpClient.post(Config.apiUrl+'updateUser', inputParam, {headers: this.getHeader()});
    }

    updateProject(inputParam : {}) {
      return this.httpClient.post(Config.apiUrl+'updateProject', inputParam, {headers: this.getHeader()});
    }

    updateParentTask(inputParam : {}) {
      return this.httpClient.post(Config.apiUrl+'updateParentTask', inputParam, {headers: this.getHeader()});
    }

    updateTask(inputParam : {}) {
      return this.httpClient.post(Config.apiUrl+'updateTask', inputParam, {headers: this.getHeader()});
    }
}
