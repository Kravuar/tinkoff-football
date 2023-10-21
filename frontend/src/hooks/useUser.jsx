import {create} from 'zustand'
import {persist} from 'zustand/middleware'

const initialUser = {
    id: 0,
    username: "",
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