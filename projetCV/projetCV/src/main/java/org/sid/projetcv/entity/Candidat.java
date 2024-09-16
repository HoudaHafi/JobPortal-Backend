package org.sid.projetcv.entity;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
 @Table(name = "candidat",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        }
)

public class Candidat implements UserDetails, Principal {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    private String email;
    @Column(unique = true)
    private String password;
    @Column(unique = true)
    private String telephone;
    private String niveau;
    private String avatar;
    private String nbexperience;
    private String dateNaissance;
    private String adresse;
    private String resetToken;

    @Override
    public String getName() {
        return getFirstName()+" " + getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    
    private List<JobApplication> jobApplications;
public String getFullName() {
    return firstName + " " + lastName;
}
}
