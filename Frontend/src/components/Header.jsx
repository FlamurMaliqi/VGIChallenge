import React from 'react';
import '../css/header.css';
import HomeLogo from "./HomeLogo";

function Header() {
    return (
        <section id="header-wrapper" className="margin-bottom-0">
            <div id="header" className="max-width-1800px">
                <div id="uvp">
                    <HomeLogo></HomeLogo>
                    <h1>Bus buchen für Schulen, Kindergärten und Gruppen in Ingolstadt und Region</h1>
                    <h2>VGI Bus Booking – Flexibel, bequem und nachhaltig ans Ziel</h2>
                    <h3>Planen Sie Ihre Klassen- oder Gruppenreise mit Leichtigkeit: VGI Bus Booking sorgt für klare
                        Kapazitätsübersichten und unterstützt dabei, die richtige Wahl zu treffen – einfach,
                        und zuverlässig.</h3>

                </div>
                <img id="header-image"
                     src={`${process.env.PUBLIC_URL}/images/BusHeaderImageV5.png`}
                     alt="Minimalist UI illustration of a school class happily boarding a white german public transport bus in a German city in a flat illustration style on a white background with bright Color scheme, dribbble, flat vector."/>
            </div>
        </section>
    );
}

export default Header;