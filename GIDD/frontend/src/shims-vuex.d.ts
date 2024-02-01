import { ComponentCustomProperties } from "vue";
import { Store } from "vuex";
import User from "@/interfaces/User.interface";

declare module "@vue/runtime-core" {
  //Decleration of State
  interface State {
    user: User;
    token: string;
    status: string;
  }

  //Typing of Store
  interface ComponentCustomProperties {
    $store: Store<State>;
  }
}
