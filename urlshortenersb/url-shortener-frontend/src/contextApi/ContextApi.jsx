import { createContext } from "react";
import { useState } from "react";
import { useContext } from "react";
const ContextApi = createContext();
export const ContextProvider = ({ children }) => {
    const [token, setToken] = useState(() => {
        const storedToken = localStorage.getItem("JWT_TOKEN");
        return storedToken ? JSON.parse(storedToken) : null;
    });

    const sendData = {
        token,
        setToken
    };

    return (
        <ContextApi.Provider value={sendData}>
            {children}
        </ContextApi.Provider>
    );
};

export const useStoreContext = () => {
    const context = useContext(ContextApi);
    return context;
};