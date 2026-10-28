package com.dundermifflin

class CompanyData {
    static final List<Map<String, Object>> EMPLOYEES = freeze([
        [name: 'Michael Scott', title: 'Regional Manager', department: 'Management',
         years_service: 15, photo: '/static/images/michael.jpg', quote: "That's what she said!"],
        [name: 'Jim Halpert', title: 'Sales Representative', department: 'Sales',
         years_service: 8, photo: '/static/images/jim.jpg', quote: 'Bears. Beets. Battlestar Galactica.'],
        [name: 'Dwight K. Schrute', title: 'Assistant Regional Manager', department: 'Sales',
         years_service: 10, photo: '/static/images/dwight.jpg', quote: 'FACT: Bears eat beets.'],
        [name: 'Pam Beesly', title: 'Office Administrator', department: 'Administration',
         years_service: 7, photo: '/static/images/pam.jpg', quote: "I'm really happy I'm here."]
    ])

    static final List<Map<String, Object>> NEWS = freeze([
        [title: 'Infinity 2.0 Launch: Revolutionary Upgrade!',
         content: 'Our new Infinity 2.0 system promises 400% more efficiency with 73% fewer bugs than the previous version. Features include: Advanced CRM integration, Mobile-first design, AI-powered paper recommendations, and Blockchain-based supply chain tracking.',
         date: '2024-01-15', author: 'Ryan Howard'],
        [title: 'Q4 Sales Records Broken Again!',
         content: 'Thanks to our innovative sales strategies and the power of Infinity 2.0, Scranton branch has exceeded all expectations. Special recognition goes to our top performers in the field.',
         date: '2024-01-10', author: 'Michael Scott'],
        [title: 'New Mobile App Available',
         content: 'Download the Dunder Mifflin Infinity 2.0 mobile app for real-time paper ordering, inventory tracking, and exclusive paper deals. Available on BlackBerry and iPhone.',
         date: '2024-01-08', author: 'IT Department']
    ])

    static final List<Map<String, Object>> OFFICES = freeze([
        [id: 'scranton', name: 'Scranton', location: 'Scranton, PA',
         available_salespeople: ['michael_scott', 'jim_halpert', 'dwight_schrute'].asImmutable()],
        [id: 'stamford', name: 'Stamford', location: 'Stamford, CT',
         available_salespeople: ['karen_filippelli', 'andy_bernard'].asImmutable()],
        [id: 'utica', name: 'Utica', location: 'Utica, NY',
         available_salespeople: ['karen_filippelli'].asImmutable()],
        [id: 'nashua', name: 'Nashua', location: 'Nashua, NH',
         available_salespeople: ['holly_flax'].asImmutable()],
        [id: 'buffalo', name: 'Buffalo', location: 'Buffalo, NY',
         available_salespeople: ['todd_packer'].asImmutable()]
    ])

    static final List<Map<String, Object>> SALESPEOPLE = freeze([
        [id: 'michael_scott', name: 'Michael Scott', title: 'Regional Manager',
         office_id: 'scranton', is_available: true, quote: "That's what she said!"],
        [id: 'jim_halpert', name: 'Jim Halpert', title: 'Sales Representative',
         office_id: 'scranton', is_available: true, quote: 'Bears. Beets. Battlestar Galactica.'],
        [id: 'dwight_schrute', name: 'Dwight K. Schrute', title: 'Assistant Regional Manager',
         office_id: 'scranton', is_available: true, quote: 'FACT: Bears eat beets.'],
        [id: 'karen_filippelli', name: 'Karen Filippelli', title: 'Sales Representative',
         office_id: 'stamford', is_available: false, quote: "I'm just trying to do my job."],
        [id: 'andy_bernard', name: 'Andy Bernard', title: 'Sales Representative',
         office_id: 'stamford', is_available: true, quote: 'I went to Cornell. Ever heard of it?'],
        [id: 'holly_flax', name: 'Holly Flax', title: 'HR Representative',
         office_id: 'nashua', is_available: true, quote: 'Why are you the way that you are?'],
        [id: 'todd_packer', name: 'Todd Packer', title: 'Traveling Salesman',
         office_id: 'buffalo', is_available: false, quote: "What's up, Halpert?"]
    ])

    private static List<Map<String, Object>> freeze(List<Map<String, Object>> rows) {
        rows.collect { it.asImmutable() }.asImmutable()
    }
}
