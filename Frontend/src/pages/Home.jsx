import React from 'react';
import Header from "../components/Header";
import QuickAccessForm from "../components/QuickAccessForm";
import ConnectionList from "../components/ConnectionList";
import Footer from "../components/Footer";

function Home() {
    return (
       <React.Fragment>
           <Header></Header>
               <section className="margin-bottom-10vh">
                   <QuickAccessForm></QuickAccessForm>
                   <ConnectionList></ConnectionList>
               </section>
           <Footer></Footer>
       </React.Fragment>
    )
}

export default Home;