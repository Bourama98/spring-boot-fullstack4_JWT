import {Wrap, WrapItem, Card, Text} from "@chakra-ui/react";
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import {Spinner} from "@chakra-ui/react"
import CardWithImage from "./components/card.jsx";
import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {

    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");
const fetchCustomers = ()=>{
    setLoading(true)
    getCustomers().then(res => {
        setCustomers(res.data)
    }).catch(err => {
        setError(err.response.data.message);
        errorNotification(
            err.code,
            err.response.data.message
        )
    }).finally(() => {
        setLoading(false)
    })
}
    useEffect(() => {
fetchCustomers();
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color="blue.500"
                    animationduration="0.8s"
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }
    if(err){
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>Ooops there was an error</Text>
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={5}>No customers available</Text>
            </SidebarWithHeader>
        )
    }
    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers={fetchCustomers}
            />
            <Wrap spacing={"30px"} justify={"center"}>
                {customers.map((customer, index) =>(
                    <WrapItem key={index}>
                        <CardWithImage
                            {...customer}
                            imageNumber={index}
                            fetchCustomers={fetchCustomers}
                        />

                    </WrapItem>
                ))}

            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;

