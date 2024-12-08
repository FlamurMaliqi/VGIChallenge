import React from 'react';
import { Link } from 'react-router-dom';

const HomeLogo = () => (
    <Link to="/" className="vgi-logo">
        <img
            className="vgi-logo"
            src={`${process.env.PUBLIC_URL}/images/vgi-logo.svg`}
            alt="VGI Logo"
        />
    </Link>
);

export default HomeLogo;
