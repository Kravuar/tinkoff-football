import {create} from 'zustand'
import {persist} from 'zustand/middleware'

const initialUser = {
    id: 0,
    login: "",
}

export const useUser = create(persist(
    (set) => ({
        user: initialUser,
        setUser: (user) => set(() => ({user})),
        invalidateUser: () => set(() => ({user: initialUser}))
    }),
    {
        name: "user-storage"
    }
))