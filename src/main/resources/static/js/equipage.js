// Attendre que le DOM soit complètement chargé
document.addEventListener('DOMContentLoaded', function() {
    // Variables globales
    let equipages = [];
    let equipageCounter = 1;
    let selectedAgents = new Set(); // Pour stocker les agents déjà assignés

    // Initialisation
    initializeEquipageHandlers();

    function initializeEquipageHandlers() {
        const creerEquipageModal = document.getElementById('creerEquipageModal');
        if (creerEquipageModal) {
            // Gestionnaire pour la réinitialisation des sélections lors de l'ouverture de la modal
            creerEquipageModal.addEventListener('show.bs.modal', function() {
                const agentSelects = this.querySelectorAll('.agent-select');
                agentSelects.forEach(select => select.selectedIndex = 0);
            });

            // Gestionnaire pour les sélections d'agents
            const agentSelects = creerEquipageModal.querySelectorAll('.agent-select');
            agentSelects.forEach(select => {
                select.addEventListener('change', function() {
                    updateAgentSelectOptions(agentSelects);
                });
            });
        }

        // Gestionnaire pour le type d'intervention
        const typeInterventionSelect = document.getElementById('typeIntervention');
        if (typeInterventionSelect) {
            typeInterventionSelect.addEventListener('change', function() {
                const formFields = ['equipageSelect', 'lieuIntervention', 'nombreInterventions'];
                formFields.forEach(fieldId => {
                    const field = document.getElementById(fieldId);
                    if (field) {
                        const formGroup = field.closest('.form-group');
                        if (formGroup) {
                            formGroup.style.display = 'block';
                            const label = formGroup.querySelector('label');
                            if (label) {
                                label.style.display = 'block';
                            }
                        } else {
                            field.style.display = 'block';
                            const label = document.querySelector(`label[for="${fieldId}"]`);
                            if (label) {
                                label.style.display = 'block';
                            }
                        }
                    }
                });
            });
        }

        // Chargement initial des agents
        loadAgents();
    }

    function loadAgents() {
        fetch('/api/agents')
            .then(response => response.json())
            .then(agents => {
               updateAgentSelects(agents);
            })
            .catch(error => console.error('Erreur chargement agents:', error));
    }

    function updateAgentSelects(agents) {
        const selects = document.querySelectorAll('.agent-select');
        selects.forEach(select => {
            const currentValue = select.value;
            select.innerHTML = '<option value="">Sélectionnez un agent</option>';
            
            agents.forEach(agent => {
                // Vérifier si l'agent n'est pas déjà sélectionné dans un équipage existant
                if (!selectedAgents.has(agent)) {
                    const option = document.createElement('option');
                    option.value = agent;
                    option.textContent = agent;
                    select.appendChild(option);
                }
            });

            // Restaurer la valeur actuelle si elle existe et est toujours disponible
            if (currentValue && !selectedAgents.has(currentValue)) {
                select.value = currentValue;
            }
        });
    }

    function updateAgentSelectOptions(selects) {
        const selectedValues = Array.from(selects).map(s => s.value);
        
        selects.forEach(select => {
            Array.from(select.options).forEach(option => {
                if (option.value === '') return; // Ignorer l'option vide
                
                // Désactiver l'option si elle est déjà sélectionnée dans un autre select
                // ou si elle est déjà utilisée dans un équipage existant
                const isSelectedInOtherSelect = selectedValues.includes(option.value) && option.value !== select.value;
                const isUsedInExistingEquipage = selectedAgents.has(option.value);
                
                option.disabled = isSelectedInOtherSelect || isUsedInExistingEquipage;
            });
        });
    }

    // Rendre les fonctions accessibles globalement
    window.sauvegarderEquipage = function(event) {
        event.preventDefault();
        const modal = document.getElementById('creerEquipageModal');
        const form = modal.querySelector('form');
        
        if (!form) return;

        const agents = Array.from(form.querySelectorAll('.agent-select'))
            .map(select => select.value)
            .filter(value => value);

        // Vérifier s'il y a au moins un agent sélectionné
        if (agents.length === 0) {
            alert('Veuillez sélectionner au moins un agent');
            return;
        }

        // Vérifier si un des agents est déjà dans un équipage
        const agentsDejaUtilises = agents.filter(agent => selectedAgents.has(agent));
        if (agentsDejaUtilises.length > 0) {
            alert(`Les agents suivants sont déjà dans un équipage : ${agentsDejaUtilises.join(', ')}`);
            return;
        }

        createEquipageCard(agents);
        const modalInstance = bootstrap.Modal.getInstance(modal);
        if (modalInstance) {
            modalInstance.hide();
        }
    };

    // Modifier la fonction createEquipageCard pour mettre à jour selectedAgents
    function createEquipageCard(agents) {
        // Ajouter les agents au Set des agents sélectionnés
        agents.forEach(agent => selectedAgents.add(agent));

        const equipageSection = document.getElementById('listeEquipages');
        const equipageDiv = document.createElement('div');
        equipageDiv.className = 'equipage-card';
        equipageDiv.style.height = '250px';

        const equipageId = `Équipage ${equipageCounter}`;
        
        const titreEquipage = document.createElement('h4');
        titreEquipage.textContent = equipageId;
        titreEquipage.style.marginBlockEnd = '15px';
        
        const agentsDiv = document.createElement('div');
        agentsDiv.style.display = 'flex';
        agentsDiv.style.flexDirection = 'column';
        agentsDiv.style.gap = '10px';
        agentsDiv.style.marginLeft = '20px';

        agents.forEach(agent => {
            const agentDiv = document.createElement('div');
            agentDiv.textContent = " - " + agent;
            agentsDiv.appendChild(agentDiv);
        });

        const actionButtonsDiv = createActionButtons(equipageId);
        equipageDiv.appendChild(titreEquipage);
        equipageDiv.appendChild(agentsDiv);
        equipageDiv.appendChild(actionButtonsDiv);
        equipageSection.appendChild(equipageDiv);

        equipageCounter++;
        updateTypeInterventionSelect();
        updateEquipageSelect();

        // Masquer le message d'avertissement
        const avertissementEquipage = document.getElementById('avertissementEquipage');
        if (avertissementEquipage) {
            avertissementEquipage.style.display = 'none';
        }

        // Mettre à jour les listes déroulantes après création
        fetch('/api/agents')
            .then(response => response.json())
            .then(agents => updateAgentSelects(agents));
    }

    function updateTypeInterventionSelect() {
        const typeInterventionSelect = document.getElementById('typeIntervention');
        if (typeInterventionSelect) {
            typeInterventionSelect.disabled = equipageCounter <= 1;
        }
    }

    // Modifier la fonction supprimerEquipage pour assurer la suppression du bon équipage
    window.supprimerEquipage = function(equipageId) {
        const equipageSection = document.getElementById('listeEquipages');
        const equipages = Array.from(equipageSection.getElementsByClassName('equipage-card'));
        
        // Vérifier si l'équipage a des interventions associées
        const tbody = document.getElementById('recapInterventions');
        const interventions = Array.from(tbody.getElementsByTagName('tr'));
        const interventionsAssociees = interventions.filter(row => 
            row.cells[0].textContent === equipageId
        );

        let messageConfirmation = "Êtes-vous sûr de vouloir supprimer cet équipage ?";
        if (interventionsAssociees.length > 0) {
            messageConfirmation = `Attention : Cet équipage a ${interventionsAssociees.length} intervention(s) associée(s). La suppression de l'équipage entraînera la suppression de ces interventions. Voulez-vous continuer ?`;
        }

        if (confirm(messageConfirmation)) {
            console.log(`✅ Suppression de l'équipage ${equipageId} confirmée`);
            
            // Supprimer d'abord les interventions associées
            if (interventionsAssociees.length > 0) {
                console.log(`🗑️ Suppression de ${interventionsAssociees.length} intervention(s) associée(s)`);
                interventionsAssociees.forEach(row => {
                    row.remove();
                    console.log(`  - Intervention supprimée pour ${equipageId}`);
                });
            }

            // Trouver l'équipage à supprimer
            const equipageToDelete = equipages.find(equipage => 
                equipage.querySelector('h4').textContent === equipageId
            );

            if (equipageToDelete) {
                // Libérer les agents
                const agentDivs = equipageToDelete.querySelectorAll('div[style*="flex-direction: column"] div');
                agentDivs.forEach(agentDiv => {
                    const agentName = agentDiv.textContent.replace(' - ', '').trim();
                    selectedAgents.delete(agentName);
                    console.log(`👤 Agent ${agentName} libéré`);
                });

                // Supprimer l'équipage
                equipageToDelete.remove();
                equipageCounter = equipageSection.getElementsByClassName('equipage-card').length + 1;

                // Réorganiser les équipages restants
                const remainingEquipages = Array.from(equipageSection.getElementsByClassName('equipage-card'));
                remainingEquipages.forEach((equipage, index) => {
                    const titre = equipage.querySelector('h4');
                    const ancienNom = titre.textContent;
                    const nouveauNom = `Équipage ${index + 1}`;
                    titre.textContent = nouveauNom;
                    console.log(`🔄 Renommage : ${ancienNom} -> ${nouveauNom}`);

                    // Mettre à jour les références dans le tableau des interventions
                    const interventionsARenommer = Array.from(tbody.getElementsByTagName('tr'))
                        .filter(row => row.cells[0].textContent === ancienNom);
                    
                    interventionsARenommer.forEach(row => {
                        row.cells[0].textContent = nouveauNom;
                        console.log(`  - Intervention mise à jour : ${ancienNom} -> ${nouveauNom}`);
                    });

                    // Mise à jour des gestionnaires d'événements
                    const supprimerBtn = equipage.querySelector('.btn-danger');
                    if (supprimerBtn) {
                        supprimerBtn.onclick = () => supprimerEquipage(nouveauNom);
                    }
                });

                // Mettre à jour l'interface
                const remainingEquipageCount = equipageSection.getElementsByClassName('equipage-card').length;
                
                if (remainingEquipageCount === 0) {
                    // Afficher le message d'avertissement
                    const avertissementEquipage = document.getElementById('avertissementEquipage');
                    if (avertissementEquipage) {
                        avertissementEquipage.style.display = 'block';
                        avertissementEquipage.style.marginTop = '20px';
                        avertissementEquipage.style.marginBottom = '20px';
                    }

                    // Masquer les champs de sélection et leurs labels
                    const formFields = ['equipageSelect', 'lieuIntervention', 'nombreInterventions'];
                    formFields.forEach(fieldId => {
                        const field = document.getElementById(fieldId);
                        if (field) {
                            const formGroup = field.closest('.form-group');
                            if (formGroup) {
                                formGroup.style.display = 'none';
                                const label = formGroup.querySelector('label');
                                if (label) {
                                    label.style.display = 'none';
                                }
                            } else {
                                field.style.display = 'none';
                                const label = document.querySelector(`label[for="${fieldId}"]`);
                                if (label) {
                                    label.style.display = 'none';
                                }
                            }
                        }
                    });
                    
                    // Réinitialiser le menu type d'intervention
                    const typeInterventionSelect = document.getElementById('typeIntervention');
                    if (typeInterventionSelect) {
                        typeInterventionSelect.selectedIndex = 0;
                        typeInterventionSelect.disabled = true;
                    }
                } else {
                    // Masquer le message d'avertissement
                    const avertissementEquipage = document.getElementById('avertissementEquipage');
                    if (avertissementEquipage) {
                        avertissementEquipage.style.display = 'none';
                    }
                    updateTypeInterventionSelect();
                    updateEquipageSelect();
                }
                
                fetch('/api/agents')
                    .then(response => response.json())
                    .then(agents => updateAgentSelects(agents))
                    .catch(error => console.error('Erreur mise à jour des agents:', error));
            }
        } else {
            console.log(`❌ Suppression de l'équipage ${equipageId} annulée`);
        }
    };

    // Nouvelle fonction pour mettre à jour la liste des équipages dans le formulaire d'intervention
    function updateEquipageSelect() {
        const equipageSelect = document.getElementById('equipageSelect');
        if (equipageSelect) {
            equipageSelect.innerHTML = '<option value="">Sélectionnez un équipage</option>';
            const equipages = document.querySelectorAll('#listeEquipages .equipage-card h4');
            equipages.forEach((equipage, index) => {
                const option = document.createElement('option');
                option.value = `equipage${index + 1}`;
                option.textContent = equipage.textContent;
                if (index === 0) {
                    option.selected = true;
                }
                equipageSelect.appendChild(option);
            });
        }
    }

    // Ajouter cette nouvelle fonction pour mettre à jour les options de la modal
    function updateModalSelectOptions() {
        fetch('/api/agents')
            .then(response => response.json())
            .then(agents => {
                const modalSelects = document.querySelectorAll('#creerEquipageModal .agent-select');
                modalSelects.forEach(select => {
                    // Sauvegarder la valeur sélectionnée actuelle
                    const currentValue = select.value;
                    select.innerHTML = '<option value="">Sélectionnez un agent</option>';
                    
                    agents.forEach(agent => {
                        // Ajouter l'agent seulement s'il n'est pas déjà dans un équipage
                        if (!selectedAgents.has(agent)) {
                            const option = document.createElement('option');
                            option.value = agent;
                            option.textContent = agent;
                            select.appendChild(option);
                        }
                    });
                });
            })
            .catch(error => console.error('Erreur mise à jour des agents:', error));
    }

    window.modifierEquipage = function(equipageId) {
        console.log("Modification de l'équipage:", equipageId);
        // Implémentation à venir
    };

    function reorganiserEquipages() {
        const equipages = document.querySelectorAll('#listeEquipages .equipage-card h4');
        equipages.forEach((equipage, index) => {
            equipage.textContent = `Équipage ${index + 1}`;
        });
    }

    function createActionButtons(equipageId) {
        const actionButtonsDiv = document.createElement('div');
        actionButtonsDiv.style.position = 'absolute';
        actionButtonsDiv.style.bottom = '10px';
        actionButtonsDiv.style.display = 'flex';
        actionButtonsDiv.style.justifyContent = 'flex-start';

        const supprimerButton = document.createElement('button');
        supprimerButton.className = 'btn btn-danger btn-sm';
        supprimerButton.innerHTML = '<i class="fas fa-trash-alt"></i>';
        supprimerButton.onclick = function() {
            console.log(`🗑️ Début de la suppression de l'équipage : ${equipageId}`);
            window.supprimerEquipage(equipageId);
        };

        actionButtonsDiv.appendChild(supprimerButton);
        return actionButtonsDiv;
    }

    // Exposer la fonction globalement
    window.createActionButtons = createActionButtons;
});
