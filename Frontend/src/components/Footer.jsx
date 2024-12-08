import React from 'react';
import '../css/footer.css';
import HomeLogo from "./HomeLogo";

function Footer() {
    return (
        <section id="footer-wrapper" className="margin-bottom-0">
            <div id="footer" className="max-width-1800px">
                <div className="horizontal-wrapper">
                    <div className="horizontal-wrapper">
                        <HomeLogo></HomeLogo>
                        <p id="vgi">Ein Service des Verkehrsverbund Großraum Ingolstadt </p>
                    </div>
                    <div className="horizontal-wrapper footer-links-wrapper">
                        <a href="https://www.vgi.de/kontakt">Kontakt</a>
                        <a href="https://www.vgi.de/bedingungen">Allgemeine Beförderungsbedingungen</a>
                        <a href="https://www.vgi.de/impressum">Impressum</a>
                        <a href="https://www.vgi.de/datenschutz">Datenschutzerklärung</a>
                    </div>
                </div>
                <div className="horizontal-wrapper">
                    <p>© 2024 Entwickelt von <a href="www.moritzschultz.de">Moritz Schultz</a> und <a
                        href="https://flamur-maliqi.com">Flamur
                        Maliqi</a></p>
                    <button>
                        <a href="/admin">Admin Dashboard</a>
                    </button>
                </div>
            </div>
        </section>
    );
}

export default Footer;