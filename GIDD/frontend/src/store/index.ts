import { createStore, Store, useStore as originalUseStore } from "vuex";
import axios from "../axiosConfig";
import User from "../interfaces/User/User.interface";
import { InjectionKey } from "vue";
import LogInUser from "@/interfaces/User/LoginUser.interface";
import { BackendStatus } from "@/enums/BackendStatus.enum";
import ICoordinates from "@/interfaces/ICoordinates.interface";

//The interface describing the State of th Vuex store
export interface State {
  status: BackendStatus;
  user: string;
  token: string;
  userLocation: string;
}

//The store needs an injection Key to provide typings when using useStore() with the composition api
export const key: InjectionKey<Store<State>> = Symbol();

//Creating the store with the type of State
export const store = createStore<State>({
  state: {
    status: BackendStatus.OK,
    token: localStorage.getItem("token") || "",
    user: localStorage.getItem("user") || "",
    userLocation: localStorage.getItem("userLocation") || "",
  },
  //The mutators methods, to mutate the state
  mutations: {
    authenticationRequest(state) {
      state.status = BackendStatus.PENDING;
    },
    authenticationSuccess(state: State, { user, token }) {
      state.user = JSON.stringify(user);
      state.token = token;
      state.status = BackendStatus.OK;
    },
    authenticationError(state: State) {
      state.status = BackendStatus.ERROR;
    },
    authenticationLogout(state) {
      state.status = BackendStatus.OK;
      state.token = "";
      state.user = "";
    },
    updateUser(state, user: User) {
      state.user = JSON.stringify(user);
    },
    updateUserLocation(state, userLocation: ICoordinates) {
      state.userLocation = JSON.stringify(userLocation);
    },
  },
  actions: {
    async login({ commit }, user: LogInUser): Promise<boolean> {
      commit("authenticationRequest");
      try {
        const response = await axios.post("/login", user);
        const token = response.data.token;
        localStorage.setItem("token", token);
        axios.defaults.headers.common["Authorization"] = token;
        const userResponse = await axios.get(`/users/${response.data.userId}`);
        localStorage.setItem("user", JSON.stringify(userResponse.data));
        commit("authenticationSuccess", { user: userResponse.data, token });
        return true;
      } catch (error) {
        commit("authenticationError");
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        delete axios.defaults.headers.common["Authorization"];
        return false;
      }
    },
    async register({ commit }, user) {
      commit("authenticationRequest");
      try {
        //TODO create and add interface to user param
        const response = await axios.post("/users", user);
        const token = response.data.token;
        localStorage.setItem("token", token);
        axios.defaults.headers["Authorization"] = token;
        const currentUser = response.data.user;
        localStorage.setItem("user", JSON.stringify(currentUser));
        commit("authenticationSuccess", { user: currentUser, token });
        return true;
      } catch (error) {
        commit("authenticationError");
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        delete axios.defaults.headers["Authorization"];
        return false;
      }
    },
    logout({ commit }) {
      commit("authenticationLogout");
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      delete axios.defaults.headers["Authorization"];
    },
    async updateUser({ commit }, user: User) {
      localStorage.setItem("user", JSON.stringify(user));
      commit("updateUser", user);
    },
    async updateUserLocation({ commit }, userLocation: ICoordinates) {
      localStorage.setItem("userLocation", JSON.stringify(userLocation));
      commit("updateUserLocation", userLocation);
    },
  },
  getters: {
    isLoggedIn: (state) => !!state.token,
    authenticationStatus: (state) => state.status,
    user: (state): User => JSON.parse(state.user),
    userLocation: (state): ICoordinates => {
      if (state.userLocation != "") {
        return JSON.parse(state.userLocation);
      }
      return {} as ICoordinates;
    },
  },
  modules: {},
});

//Configuring our own useStore method so that typing will be available
export function useStore(): Store<State> {
  return originalUseStore(key);
}
