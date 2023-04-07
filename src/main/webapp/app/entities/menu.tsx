import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/theatres">
        Theatres
      </MenuItem>
      <MenuItem icon="asterisk" to="/movie-show">
        Movie Show
      </MenuItem>
      <MenuItem icon="asterisk" to="/movie">
        Movie
      </MenuItem>
      <MenuItem icon="asterisk" to="/seat">
        Seat
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
